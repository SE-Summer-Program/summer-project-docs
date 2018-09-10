import { Layout, Menu, Select, Table, Button } from 'antd';
import React, { Component } from 'react';
import './../App.css';
import context from "../context";


const {Content} = Layout;
const Option = Select.Option;
const stationData=["徐汇校区","闵行校区","七宝校区"];


class SearchOutShift extends React.Component {
    constructor(props){
        super(props);
        this.state={
            date:'',
            direction:'',
            isHoliday:'',
            isWorkday:'',
            startStation:'',
            endStation:'',
            data:[],
            count:0,
        };
        this.columns = [{
            title: '班次编号',
            dataIndex: 'shiftid',
            key: 'shiftid',
            width: '18%',
            align: 'center',
        },{
            title: '始发站',
            dataIndex: 'startStation',
            key: 'startStation',
            width: '20%',
            align: 'center',
        }, {
            title: '终点站',
            dataIndex: 'endStation',
            key: 'endStation',
            width: '20%',
            align: 'center',
        }, {
            title: '出发时刻',
            dataIndex: 'startTime' ,
            key: 'startTime',
            width: '20%',
            align: 'center',
        }, {
            title: '备注',
            dataIndex: 'comment' ,
            key: 'comment',
            width: '20%',
            align: 'center',
        }];
    }


    handleStartStationChange = (value) => {
        this.setState({
            startStation: value,
        });
        console.log(value)
    };

    handleEndStationChange = (value) => {
        this.setState({
            endStation: value,
        });
    };

    handleHolidayChange =(value) => {
        if (value === "true"){
            this.setState({
                isHoliday: "true"
            })
        }
        else{
            this.setState({
                isHoliday: "false"
            })
        }
    };

    handleWorkdayChange =(value) => {
        if (value === "true"){
            this.setState({
                isWorkday: "true"
            })
        }
        else{
            this.setState({
                isWorkday: "false"
            })
        }
    };

    handleSearch=() =>{
        let startStation = this.state.startStation;
        let endStation = this.state.endStation;
        let isHoliday = this.state.isHoliday;
        let isWorkday = this.state.isWorkday;

        if(startStation === ''){
            alert("请选择始发站");
            return;
        }
        if(endStation === ''){
            alert("请选择终点站");
            return;
        }
        if (startStation === endStation){
            alert("始发站和终点站不能相同");
            return;
        }
        if(isHoliday === ''){
            alert("请选择是否是寒暑假");
            return;
        }
        if(isWorkday === ''){
            alert("请选择是否是工作日");
            return;
        }

        this.setState({
            data:[],
            count:0,
        });
        let start='';
        let end='';
        let type='';
        if (startStation === '徐汇校区')
            start = 'XuHui';
        else if(startStation === '闵行校区')
            start = 'MinHang';
        else
            start = 'QiBao';

        if (endStation === '徐汇校区')
            end = 'XuHui';
        else if (endStation === '闵行校区')
            end = 'MinHang';
        else
            end = 'QiBao';

        if ((isWorkday === 'true') && (isHoliday==='true'))
            type = 'HolidayWorkday';
        else if ((isWorkday === 'true') && (isHoliday==='false'))
            type = 'NormalWorkday';
        else if ((isWorkday === 'false') && (isHoliday==='true'))
            type = 'HolidayWeekend';
        else if ((isWorkday === 'false') && (isHoliday === 'false'))
            type = 'NormalWeekendAndLegalHoliday';

        let temproute= 'line_name='+ start +'To'+ end + '&type=' + type;
        console.log("route:", temproute);

        fetch(context.api+'/shift/search_schedule?'+temproute,
            {
                method: 'POST',
                mode: 'cors',
            })
            .then(response => {
                console.log('Request successful', response);
                return response.json()
                    .then(result => {
                        let len = result.schedule.scheduleShift.length;
                        console.log("response len:",len);
                        this.state.data=[];
                        for (let i=0; i < len; i++) {
                            const {data,count}=this.state;
                            const add = {
                                "key": this.state.count+1,
                                "shiftid": result.schedule.scheduleShift[i],
                                "startTime": result.schedule.scheduleTime[i],
                                "comment": result.schedule.scheduleComment[i],
                                "startStation": this.state.startStation,
                                "endStation": this.state.endStation,
                            };

                            this.setState({
                                data: [...data, add],
                                count: count+1,
                            });
                        }
                    })
            });
    };

    render(){
        const stationOptions = stationData.map(station => <Option key={station}>{station}</Option>);
        return(
            <Layout>
                {context.header('3')}
                <Content style={{ marginLeft:'3%', marginRight:'3%' }}>
                    {context.breadcrumb("信息查询","查询校区巴士班次表")}
                    <Layout style={{ padding: '1.5% 1%', background: '#fff' }}>
                        {context.sider_search("sub3","6")}
                        <Content style={{ padding: '1% 1.5%', minHeight: 280 }}>
                            <br/>
                            <Select defaultValue="始发站" size="large" style={{marginLeft:'15%', width:'14%'}} onChange={this.handleStartStationChange}>
                                {stationOptions}
                            </Select>
                            <Select defaultValue="终点站" size="large" style={{marginLeft:'1%', width:'14%'}} onChange={this.handleEndStationChange}>
                                {stationOptions}
                            </Select>
                            <Select defaultValue="是否寒暑假" size="large" style={{marginLeft:'1%', width:'13%'}} onChange={this.handleHolidayChange}>
                                <Option value="true">寒暑假</Option>
                                <Option value="false">非寒暑假</Option>
                            </Select>
                            <Select defaultValue="是否工作日" size="large" style={{marginLeft:'1%', width:'13%'}} onChange={this.handleWorkdayChange}>
                                <Option value="true">工作日</Option>
                                <Option value="false">节假日</Option>
                            </Select>
                            <Button type="primary"  size="large" style={{width: '10%', marginLeft: '1.5%'}} icon="search" onClick = {this.handleSearch}>搜索</Button>
                            <h1 />
                            <br />
                            <Table style={{width:'88%', marginLeft:'7%'}} columns={this.columns} dataSource={this.state.data} />
                        </Content>
                    </Layout>
                </Content>
                {context.footer}
            </Layout>
        );
    }
}

export default SearchOutShift;
