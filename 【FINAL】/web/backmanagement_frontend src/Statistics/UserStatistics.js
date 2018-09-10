import { Layout, Menu, Breadcrumb, Icon, Radio, Select, Button, DatePicker, Row, Col, Card } from 'antd';
import React, { Component } from 'react';
import './../App.css';
import context from "../context";
import moment from 'moment';
import zh_CN from 'antd/lib/locale-provider/zh_CN';
import 'moment/locale/zh-cn';


const {Content} = Layout;
const { MonthPicker, RangePicker } = DatePicker;
const dateFormat = 'YYYY/MM/DD';
const monthFormat = 'YYYY/MM';
const Option = Select.Option;
const stationData=["徐汇","闵行","七宝"];
const RadioGroup = Radio.Group;
const method=["按月统计","自选时间"];
const Highcharts = require('highcharts');

class UserStatistics extends React.Component {
    constructor(props){
        super(props);
        this.state={
            month:'',
            startDate:'',
            endDate:'',
            type:'',
            startStation:'',
            endStation:'',
            timeData:[],
            time:'',
            data:[],
            method:'',
            disabled:false,
            loading:false,
            button_text:'生成统计数据',
        };
    }

    handleMethodChange = (e)=> {
        console.log("method:",e.target.value);
        if(e.target.value === "按月统计"){
            this.setState({
                disabled: false,
                method:'month'
            })
        }
        else{
            this.setState({
                disabled: true,
                method:'defined'
            })
        }
    };

    disabledDate=(current) => {
        return current && current> moment();
    };

    handleMonthChange=(date,dateString) => {
        console.log("date:", date);
        let monthStartDate = date.startOf('month').format('L');
        let monthEndDate = date.endOf('month').format('L');
        this.setState({
            month: dateString,
            monthStartDate: monthStartDate,
            monthEndDate: monthEndDate
        })
    };

    handleDateChange = (dates, dateStrings) => {
        console.log("date:", dates);
        let str1 = dateStrings[0].replace(/\//g,"-");
        let str2 = dateStrings[1].replace(/\//g,"-");
        this.setState({
            startDate: str1,
            endDate: str2,
        });
        console.log("date1:",dateStrings[0]);
        console.log("date2:",dateStrings[1]);
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

    handleTypeChange = (value) => {
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

    };

    handleTimeChange = (value) => {
        this.setState({
            time: value,
        });
    };

    handleClick = () => {
        if (this.state.startStation === this.state.endStation) {
            alert("始发站和终点站不能相同");
            return;
        }
        this.setState({
            loading: true,
            button_text: '正在生成..'
        }, () => {
            let route = context.api + '/statistics/ridebusinfo';
            let lineNameCn = this.state.startStation + "到" + this.state.endStation;
            route += '?startDate=' + (this.state.method === "month" ? this.state.monthStartDate : this.state.startDate).replace(new RegExp("/", "gm"), "-")
                + '&endDate=' + (this.state.method === "month" ? this.state.monthEndDate : this.state.endDate).replace(new RegExp("/", "gm"), "-")
                + '&lineNameCn=' + lineNameCn + '&lineType=' + this.state.type + '&time=' + this.state.time;
            console.log("route:", route);
            fetch(route,
                {
                    method: 'POST',
                    mode: 'cors',
                })
                .then(response => {
                    console.log('Request successful', response);
                    return response.json()
                        .then(result => {
                            console.log(result);
                            if(result.rideBusInfos==null||result.rideBusInfos.length===0){
                                alert("无数据!");
                                return;
                            }
                            let seatNum = result.rideBusInfos.length !== 0 ? result.rideBusInfos[0].seatNum : 0;
                            let appointBreakSum = 0;
                            let studentArray = [];
                            let teacherArray = [];
                            let studentSum = 0;
                            let teacherSum = 0;
                            for (let i = 0; i < result.rideBusInfos.length; i++) {
                                studentArray.push(result.rideBusInfos[i].studentNum);
                                teacherArray.push(result.rideBusInfos[i].teacherNum);
                                studentSum += result.rideBusInfos[i].studentNum;
                                teacherSum += result.rideBusInfos[i].teacherNum;
                                appointBreakSum += result.rideBusInfos[i].appointBreak;
                            }
                            this.setState({
                                seatNum: seatNum,
                                avgAboardNum: (studentSum + teacherSum) / result.rideBusInfos.length,
                                appointBreakSum: appointBreakSum,
                                avgStudentNum: studentSum / result.rideBusInfos.length,
                                avgTeacherNum: teacherSum / result.rideBusInfos.length,
                                maxStudentNum: Math.max.apply(Math, studentArray),
                                minStudentNum: Math.min.apply(Math, studentArray),
                                maxTeacherNum: Math.max.apply(Math, teacherArray),
                                minTeacherNum: Math.min.apply(Math, teacherArray),
                            });
                            Highcharts.chart('seatRate', {
                                chart: {
                                    type: 'pie'
                                },
                                title: {
                                    text: '平均入座情况'
                                },
                                tooltip: {
                                    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                                },
                                plotOptions: {
                                    pie: {
                                        allowPointSelect: true,
                                        cursor: 'pointer',
                                        dataLabels: {
                                            enabled: true,
                                            format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                                        }
                                    }
                                },
                                series: [{
                                    name: '入座比例',
                                    colorByPoint: true,
                                    data: [{
                                        name: '教职工',
                                        y: teacherSum / result.rideBusInfos.length
                                    }, {
                                        name: '非教职工',
                                        y: studentSum / result.rideBusInfos.length
                                    }, {
                                        name: '空位',
                                        y: seatNum - (studentSum + teacherSum) / result.rideBusInfos.length
                                    }]
                                }]
                            });
                            Highcharts.chart('appointRate', {
                                chart: {
                                    type: 'pie'
                                },
                                title: {
                                    text: '总体预约出勤情况'
                                },
                                tooltip: {
                                    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                                },
                                plotOptions: {
                                    pie: {
                                        allowPointSelect: true,
                                        cursor: 'pointer',
                                        dataLabels: {
                                            enabled: true,
                                            format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                                        }
                                    }
                                },
                                series: [{
                                    name: '比例',
                                    colorByPoint: true,
                                    data: [{
                                        name: '按时到达',
                                        y: studentSum + teacherSum
                                    }, {
                                        name: '未到达',
                                        y: appointBreakSum
                                    }]
                                }]
                            });
                        })
                        .then(() => {
                            this.setState({
                                loading: false,
                                button_text: '生成统计数据'
                            })

                        })
                });
        })
    };

    render(){
        const timeData = this.state.timeData;
        const stationOptions = stationData.map(station => <Option key={station}>{station}</Option>);
        let timeOptions = timeData.map(time => <Option key={time}>{time}</Option>);
        return(
            <Layout>
                {context.header('4')}
                <Content style={{ marginLeft:'3%', marginRight:'3%' }}>
                    {context.breadcrumb("信息统计","乘客信息统计")}
                    <Layout style={{ padding: '24px 0', background: '#fff' }}>
                        {context.sider_statistics("1")}
                        <Content style={{ padding: '0 24px', minHeight: 280 }}>
                            <br/>
                            <h1 style={{marginLeft:'39%', fontWeight:"bold"}}>已发车次乘客统计</h1>
                            <br/>
                            <h1/>
                            <span style={{marginLeft: '22%', fontSize:'120%'}}>选择统计方式： </span>
                            <RadioGroup options={method} size="large"  onChange={this.handleMethodChange}/>
                            <br/>
                            <br/>
                            <h6/>
                            <span style={{marginLeft:"22%", fontSize:'120%'}}> 选择月份： </span>
                            <MonthPicker locale={zh_CN} defaultValue={moment(moment(), monthFormat)} disabled={this.state.disabled}
                                         disabledDate={this.disabledDate} format={monthFormat} size="large" onChange={this.handleMonthChange} />
                            <h6 />
                            <br />
                            <span style={{marginLeft:"22%", fontSize:'120%'}}> 自定义时间段： </span>
                            <RangePicker locale={zh_CN} disabled={!this.state.disabled} disabledDate={this.disabledDate}
                                         format={dateFormat} size="large" onChange={this.handleDateChange}/>
                            <h6 />
                            <br />
                            <Select defaultValue="始发站" size="large" style={{marginLeft:'22%', width:'140px'}} onChange={this.handleStartStationChange}>
                                {stationOptions}
                            </Select>
                            <Select defaultValue="终点站" size="large" style={{marginLeft:'1%', width:'140px'}} onChange={this.handleEndStationChange}>
                                {stationOptions}
                            </Select>
                            <Select defaultValue="线路类型" size="large" style={{marginLeft:'1%', width:'150px'}} onSelect={this.handleTypeChange}>
                                <Option value="NormalWorkday">普通工作日</Option>
                                <Option value="HolidayWorkday">寒暑假工作日</Option>
                                <Option value="HolidayWeekend">寒暑假双休日</Option>
                                <Option value="NormalWeekendAndLegalHoliday">普通节假双休日</Option>
                            </Select>
                            <Select defaultValue="出发时刻" size="large" style={{marginLeft:'1%', width:'140px'}} onChange={this.handleTimeChange}>
                                {timeOptions}
                            </Select>
                            <h6 />
                            <br />
                            <Button type="primary" size="large" style={{marginLeft:"43%"}} onClick={this.handleClick}>生成统计数据</Button>
                            <h6 />
                            <br />
                            <span style={{marginLeft:"15%"}}>-----------------------------------------------------------------------------------------------------------------------------</span>
                            <h6 />
                            <br />
                            <div style={{ background: '#ECECEC', padding: '20px' }}>
                                <Row gutter={16}>
                                    <Col span={8}>
                                        <Card title="核载人数" hoverable='true' bordered='true'>{this.state.seatNum}</Card>
                                    </Col>
                                    <Col span={8}>
                                        <Card title="平均实际上车人数" hoverable='true' bordered='true'>{this.state.avgAboardNum}</Card>
                                    </Col>
                                    <Col span={8}>
                                        <Card title="预约未上车总人数" hoverable='true' bordered='true'>{this.state.appointBreakSum}</Card>
                                    </Col>
                                </Row>
                                <Row gutter={16}>
                                    <Col span={8}>
                                        <Card title="教工平均乘坐人数" hoverable='true' bordered='true'>{this.state.avgTeacherNum}</Card>
                                    </Col>
                                    <Col span={8}>
                                        <Card title="教工乘坐最大人数" hoverable='true' bordered='true'>{this.state.maxTeacherNum}</Card>
                                    </Col>
                                    <Col span={8}>
                                        <Card title="教工乘坐最小人数" hoverable='true' bordered='true'>{this.state.minTeacherNum}</Card>
                                    </Col>
                                </Row>
                                <Row gutter={16}>
                                    <Col span={8}>
                                        <Card title="非教工平均乘坐人数" hoverable='true' bordered='true'>{this.state.avgStudentNum}</Card>
                                    </Col>
                                    <Col span={8}>
                                        <Card title="非教工乘坐最大人数" hoverable='true' bordered='true'>{this.state.maxStudentNum}</Card>
                                    </Col>
                                    <Col span={8}>
                                        <Card title="非教工乘坐最小人数"  hoverable='true' bordered='true'>{this.state.minStudentNum}</Card>
                                    </Col>
                                </Row>
                                <br/>
                                <span style={{marginLeft: '44.5%', fontSize:'180%', fontWeight:'bold'}}>统计结果</span>
                                <br/>
                                <h1/>
                                <Row gutter={16}>
                                    <Col span={12}>
                                        <div id="seatRate"/>
                                        {/*<Card title="平均入座率(上车人数/核载人数)" hoverable='true' bordered='true'>content</Card>*/}
                                    </Col>
                                    <Col span={12}>
                                        <div id="appointRate"/>
                                        {/*<Card title="预约乘客平均入座率(预约上车人数/预约人数)" hoverable='true' bordered='true'>content</Card>*/}
                                    </Col>
                                </Row>
                                <Row gutter={16}>
                                    <Col span={12} style={{marginLeft: '25%'}}>
                                        <Card title="推荐预留座位数" hoverable='true' bordered='true'>content</Card>
                                    </Col>
                                </Row>
                            </div>
                        </Content>
                    </Layout>
                </Content>
                {context.footer}
            </Layout>
        );
    }
}

export default UserStatistics;
