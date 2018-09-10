package com.sjtubus.utils;

import com.baidu.mapapi.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BusLocationSimulator {
    private Double totalTime = 1080d;//校车开一圈用时为18分钟
    private Double totalLength = 0d;//线路总长度
    private List<Double> distance = new ArrayList<>();//存放所有相邻距离
    private List<String> AntiClockwise = new ArrayList<>();
    private List<String> Clockwise = new ArrayList<>();
    public List<LatLng> points = new ArrayList<>();//存放所有途经点的位置
    public class BusLocation{
        public LatLng location;
        public float rotate;
        public BusLocation(LatLng location,float rotate){
            this.location = location;
            this.rotate = rotate;
        }
    }

    public BusLocationSimulator(){
        setData();
    }

    public List<BusLocation> getBusLocation(String time){
        List<BusLocation> result = new ArrayList<BusLocation>();
        //计算当前巴士已运行的时间
        SimpleDateFormat simpleFormat = new SimpleDateFormat("HH-mm-ss");
        Double s1 = -1d;//s1为较早发车的班次
        Double s2 = -1d;//s2不一定存在
        Double s3 = -1d;//s3为顺时针班次
        for(String tmp : AntiClockwise){
            try {
                int s = (int) ((simpleFormat.parse(time).getTime() - simpleFormat.parse(tmp).getTime()) / 1000);
                if((s < totalTime) && (s > 0)){
                    if(s1 < 0) s1 = s / totalTime * totalLength;
                    else s2 = s / totalTime * totalLength;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        for(String tmp : Clockwise){
            try {
                int s = (int) ((simpleFormat.parse(time).getTime() - simpleFormat.parse(tmp).getTime()) / 1000);
                if((s < totalTime) && (s > 0)){
                    s3 = s / totalTime * totalLength;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
        //计算已经行驶到何处
        result.add(CalculateLocation(s1, "Anti"));
        result.add(CalculateLocation(s2, "Anti"));
        result.add(CalculateLocation(s3, "Along"));

        return result;
    }

    private BusLocation CalculateLocation(Double length, String type){
        BusLocation result = new BusLocation(new LatLng(31.024697,121.436855),0);//初始点设为停车场
        if(length < 0) return result;
        int start = 0, end = distance.size(), step = 1;
        if(type == "Along"){
            start = distance.size() - 1;
            end = -1;
            step = -1;
        }
        for(int i = start; i != end; i += step){
            if(length < distance.get(i)){
                Double latitude_tmp = (points.get(i + step).latitude - points.get(i).latitude) * length / distance.get(i) + points.get(i).latitude;
                Double longitude_tmp = (points.get(i + step).longitude - points.get(i).longitude) * length / distance.get(i) + points.get(i).longitude;
                Float rotate = Rotate(points.get(i), points.get(i + step));
                result = new BusLocation(new LatLng(latitude_tmp, longitude_tmp), rotate);
                break;
            }
            else length -= distance.get(i);
        }
        return result;
    }

    private Float Rotate(LatLng x, LatLng y){
        Double v = (y.latitude - x.latitude) / (y.longitude - x.longitude);
        float result = (float) (Math.atan(v) / Math.PI * 180);
        if(y.longitude < x.longitude) result += 180;
        return result;
    }

    private void setData(){
        //载入途经点
        points.add(new LatLng(31.024766399515144,121.43630746466236));
        points.add(new LatLng(31.024959811937066,121.43699915990504));
        points.add(new LatLng(31.025209,121.437614));
        points.add(new LatLng(31.025499,121.43805));
        points.add(new LatLng(31.025592,121.438333));
        points.add(new LatLng(31.025658,121.438836));
        points.add(new LatLng(31.025864976800392,121.43990068670226));
        points.add(new LatLng(31.026064,121.440844));
        points.add(new LatLng(31.027466401048258,121.44552407919474));
        points.add(new LatLng(31.031628429647498,121.44371848511967));
        points.add(new LatLng(31.033098247158424,121.44829984322057));
        points.add(new LatLng(31.032547,121.4483));
        points.add(new LatLng(31.031615,121.448686));
        points.add(new LatLng(31.030996,121.449414));
        points.add(new LatLng(31.030684640009543,121.4510666241913));
        points.add(new LatLng(31.029361771422135,121.45170442110339));
        points.add(new LatLng(31.03035,121.454732));
        points.add(new LatLng(31.030559,121.454965));
        points.add(new LatLng(31.030872,121.455113));
        points.add(new LatLng(31.031294,121.455122));
        points.add(new LatLng(31.03251805877257,121.45457899873531));
        points.add(new LatLng(31.03502444699885,121.45342018462745));
        points.add(new LatLng(31.035908,121.452742));
        points.add(new LatLng(31.03682683932345,121.4513720480647));
        points.add(new LatLng(31.037332,121.449881));
        points.add(new LatLng(31.037252291712417,121.44823696183487));
        points.add(new LatLng(31.034390120473216,121.43949644922279));
        points.add(new LatLng(31.032208623507113,121.43292983594485));
        points.add(new LatLng(31.03071558408187,121.43357661591203));
        points.add(new LatLng(31.031705789035033,121.43656797326025));
        points.add(new LatLng(31.029214784878082,121.43755610932124));
        points.add(new LatLng(31.02816266397923,121.4344839044771));
        points.add(new LatLng(31.024766399515144,121.43630746466236));//终点与起点重合
        //计算每两个相邻点的距离及总长度
        for(int i = 0; i < points.size() - 1; i++){
            Double distance_tmp;
            distance_tmp = Math.sqrt(Math.pow(points.get(i).latitude - points.get(i + 1).latitude, 2)
                    + Math.pow(points.get(i).longitude - points.get(i + 1).longitude, 2));
            distance.add(distance_tmp);
            totalLength += distance_tmp;
        }
        //载入首发时刻
        AntiClockwise.add("07-30-00");
        AntiClockwise.add("07-45-00");
        AntiClockwise.add("08-00-00");
        AntiClockwise.add("08-15-00");
        AntiClockwise.add("08-25-00");
        AntiClockwise.add("08-40-00");
        AntiClockwise.add("09-00-00");
        AntiClockwise.add("09-20-00");
        AntiClockwise.add("09-40-00");
        AntiClockwise.add("10-00-00");
        AntiClockwise.add("10-20-00");
        AntiClockwise.add("10-40-00");
        AntiClockwise.add("11-00-00");
        AntiClockwise.add("11-20-00");
        AntiClockwise.add("11-40-00");
        AntiClockwise.add("12-00-00");
        AntiClockwise.add("13-00-00");
        AntiClockwise.add("13-20-00");
        AntiClockwise.add("13-40-00");
        AntiClockwise.add("14-00-00");
        AntiClockwise.add("14-20-00");
        AntiClockwise.add("14-40-00");
        AntiClockwise.add("15-00-00");
        AntiClockwise.add("15-20-00");
        AntiClockwise.add("15-40-00");
        AntiClockwise.add("16-00-00");
        AntiClockwise.add("16-20-00");
        AntiClockwise.add("16-30-00");
        AntiClockwise.add("17-00-00");
        AntiClockwise.add("17-15-00");
        AntiClockwise.add("17-30-00");
        AntiClockwise.add("17-50-00");
        AntiClockwise.add("18-00-00");
        AntiClockwise.add("19-00-00");
        AntiClockwise.add("20-10-00");

        Clockwise.add("08-30-00");
        Clockwise.add("08-50-00");
        Clockwise.add("09-10-00");
        Clockwise.add("09-30-00");
        Clockwise.add("10-00-00");
        Clockwise.add("10-30-00");
        Clockwise.add("11-00-00");
        Clockwise.add("11-30-00");
        Clockwise.add("12-30-00");
        Clockwise.add("13-30-00");
        Clockwise.add("14-00-00");
        Clockwise.add("14-30-00");
        Clockwise.add("15-00-00");
        Clockwise.add("15-30-00");
        Clockwise.add("16-00-00");
        Clockwise.add("16-30-00");
    }
}
