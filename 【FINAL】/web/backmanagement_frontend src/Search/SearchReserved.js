import { Layout, Menu, Select, Table, DatePicker, Button } from 'antd';
import React, { Component } from 'react';
import './../App.css';
import context from "../context";

const {Content} = Layout;
const Option = Select.Option;
const stationData=["徐汇","闵行","七宝"];

class SearchReserved extends React.Component {
    constructor(props){
        super(props);
        this.state={
            date:'',
            time:'',
            type:'',
            startStation:'',
            endStation:'',
            timeData:[],
            data:[],
            count:0,
        };
        this.columns = [{
            title: '班次编号',
            dataIndex: 'shiftid',
            key: 'shiftid',
            width: '15%',
            align: 'center',
        },{
            title: '预约编号',
            dataIndex: 'reservedid',
            key: 'reservedid',
            width: '15%',
            align: 'center',
        },{
            title: '学生id',
            dataIndex: 'studentid',
            key: 'studentid',
            width: '15%',
            align: 'center',
        }, {
            title: '学生姓名',
            dataIndex: 'studentname',
            key: 'studentname',
            width: '15%',
            align: 'center',
        }, {
            title: '提交时间',
            dataIndex: 'submittime',
            key: 'submittime',
            width: '20%',
            align: 'center',
        },{
            title: '是否正常',
            dataIndex: 'isNormal' ,
            key: 'isNormal',
            width: '2 0%',
            align: 'center',
        }];
    }

    onDateChange = (date, dateString) => {
        this.setState({
            date: dateString
        });
        console.log(dateString);
    };

    fetch_time = (startStation, endStation, lineType)=>{
        let lineName = startStation + "到" + endStation;
        let timeString=[];
        console.log("route:", context.api+'/shift/search_time?lineNameCn=' + lineName + "&lineType=" + lineType);
        fetch(context.api+'/shift/search_time?lineNameCn=' + lineName + "&lineType=" + lineType,
            {
                method: 'POST',
                mode: 'cors',
            })
            .then(response => {
                console.log('Request successful', response);
                return response.json()
                    .then(result => {
                        let len = result.timeList.length;
                        console.log("response len:", len);
                        for (let i = 0; i < len; i++) {
                            let add = result.timeList[i];
                            timeString.push(add);
                        }
                        this.setState({
                            timeData: timeString
                        })
                    })
            });

    };


    handleTypeChange = (value) => {
        /*this.setState({
            type: value,
        }, function () {
            this.fetch_time(this.state.startStation, this.state.endStation, this.state.type),()=>{
                console.log("timeData:",this.state.timeData);
            }*/
        let start = this.state.startStation;
        let end = this.state.endStation;
        if (start === "" || end === ""){
            this.setState({type:value});
        }
        else{
            this.setState({
                type: value,
            }, function () {
                this.fetch_time(start, end, this.state.type)
            })
        }


            /*let lineName = this.state.startStation + "到" + this.state.endStation;
            console.log("route:", context.api+'/shift/search_time?lineNameCn=' + lineName + "&lineType=" + this.state.type);
            fetch(context.api+'/shift/search_time?lineNameCn=' + lineName + "&lineType=" + this.state.type,
                {
                    method: 'POST',
                    mode: 'cors',
                })
                .then(response => {
                    console.log('Request successful', response);
                    return response.json()
                        .then(result => {
                            let len = result.timeList.length;
                            console.log("response len:", len);
                            this.state.timeData = [];
                            for (let i = 0; i < len; i++) {
                                const {timeData} = this.state;
                                console.log(result.timeList[i]);
                                const add = result.timeList[i];
                                this.setState({
                                    timeData: [...timeData, add],
                                });
                            }
                        })
                });*/
    };

    handleTimeChange = (value) => {
        this.setState({
            time: value,
        });
    };


    handleStartStationChange = (value) => {
        let type = this.state.type;
        let end = this.state.endStation;
        if (type === "" || end === ""){
            this.setState({startStation:value});
        }
        else{
            this.setState({
                startStation: value,
            }, function () {
                this.fetch_time(value, end,type)
            })
        }
    };

    handleEndStationChange = (value) => {
        let type = this.state.type;
        let start= this.state.startStation;
        if (type === "" || start === ""){
            this.setState({endStation:value});
        }
        else{
            this.setState({
                endStation: value,
            }, function () {
                this.fetch_time(start, value, type)
            })
        }
    };

    handleSearch=() =>{
        if(this.state.date === ''){
            alert("请选择预约的日期");
            return;
        }
        if(this.state.startStation === ''){
            alert("请选择始发站");
            return;
        }
        if(this.state.endStation === ''){
            alert("请选择终点站");
            return;
        }
        if (this.state.startStation === this.state.endStation){
            alert("始发站和终点站不能相同");
            return;
        }
        if(this.state.type === ''){
            alert("请选择线路类型");
            return;
        }
        if (this.state.time === ''){
            alert("请选择发车时间");
            return;
        }


        this.setState({
            data:[],
            count:0,
        });
        let lineNameCn = this.state.startStation + "到" + this.state.endStation;
        fetch(context.api+'/appointment/search?lineNameCn='+ lineNameCn + "&lineType=" + this.state.type +
                                         "&departureTime=" + this.state.time + "&appointDate=" + this.state.date,
            {
                method: 'POST',
                mode: 'cors',
            })
            .then(response => {
                console.log('Request successful', response);
                return response.json()
                    .then(result => {
                        if (result.error === 1){
                            alert("查询失败");
                        }
                        else{
                            let len = result.appointmentList.length;
                            console.log("response len:",len);
                            this.state.data=[];
                            for (var i=0; i < len; i++) {
                                const {data,count}=this.state;
                                let normal = '';
                                if (result.appointmentList[i].normal === true) {
                                    normal = "正常";
                                }
                                else{
                                    normal = "非正常"
                                }
                                const add = {
                                    "key": this.state.count+1,
                                    "shiftid": result.appointmentList[i].shiftId,
                                    "reservedid": result.appointmentList[i].appointmentId,
                                    "studentid": result.appointmentList[i].userId,
                                    "studentname": result.appointmentList[i].userName,
                                    "submittime": result.appointmentList[i].submitTime,
                                    "isNormal": normal,
                                };
                                this.setState({
                                    data: [...data, add],
                                    count: count+1,
                                });
                            }
                        }
                    })
            });
    };

    render(){
        const timeData = this.state.timeData;
        const stationOptions = stationData.map(station => <Option key={station}>{station}</Option>);
        let timeOptions = timeData.map(time => <Option key={time}>{time}</Option>);
        return(
            <Layout>
                {context.header('3')}
                <Content style={{ marginLeft:'3%', marginRight:'3%' }}>
                    {context.breadcrumb("信息查询","查询预约信息")}
                    <Layout style={{ padding: '1.5% 1%', background: '#fff' }}>
                        {context.sider_search("sub3","5")}
                        <Content style={{ padding: '1% 1.5%', minHeight: 280 }}>
                            <br/>
                            <DatePicker size="large" style={{marginLeft:'2%'}} onChange={this.onDateChange} />
                            <Select defaultValue="始发站" size="large" style={{marginLeft:'1%', width:'14%'}} onChange={this.handleStartStationChange}>
                                {stationOptions}
                            </Select>
                            <Select defaultValue="终点站" size="large" style={{marginLeft:'1%', width:'14%'}} onChange={this.handleEndStationChange}>
                                {stationOptions}
                            </Select>
                            <Select defaultValue="线路类型" size="large" style={{marginLeft:'1%', width:'15%'}} onSelect={this.handleTypeChange}>
                                <Option value="NormalWorkday">普通工作日</Option>
                                <Option value="HolidayWorkday">寒暑假工作日</Option>
                                <Option value="HolidayWeekend">寒暑假双休日</Option>
                                <Option value="NormalWeekendAndLegalHoliday">普通节假双休日</Option>
                            </Select>
                            <Select defaultValue="出发时刻" size="large" style={{marginLeft:'1%', width:'14%'}} onChange={this.handleTimeChange}>
                                {timeOptions}
                            </Select>
                            <Button type="primary"  size="large" style={{width: '10%', marginLeft: '2%'}} icon="search" onClick = {this.handleSearch}>搜索</Button>
                            <h1 />
                            <br />
                            <Table style={{width:'75%', marginLeft:'12%'}} columns={this.columns} dataSource={this.state.data} />
                        </Content>
                    </Layout>
                </Content>
                {context.footer}
            </Layout>
        );
    }

}

export default SearchReserved;
