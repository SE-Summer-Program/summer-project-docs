import { Layout, Select, Table, Button } from 'antd';
import React, { Component } from 'react';
import './../App.css';
import context from "../context";

const { Content} = Layout;
const Option = Select.Option;
const stationData=["菁菁堂","东川路地铁站"];
const directionData=["顺时针","逆时针"]

class SearchInShift extends React.Component {
    constructor(props){
        super(props);
        this.state={
            date:'',
            direction:'',
            isHoliday:'',
            isWorkday:'',
            startStation:'',
            EndStation:'',
            count:0,
            data:[],
        };
        this.columns = [{
            title: '',
            dataIndex: '',
            key: '',
            width: '15%',
            align: 'center',
        },{
            title: '班次编号',
            dataIndex: 'shiftid',
            key: 'shiftid',
            width: '20%',
            align: 'center',
        },{
            title: '方向',
            dataIndex: 'direction',
            key: 'direction',
            width: '18%',
            align: 'center',
        },{
            title: '出发时刻',
            dataIndex: 'startTime' ,
            key: 'startTime',
            width: '20%',
            align: 'center',
        },{
            title: '备注',
            dataIndex: 'comment' ,
            key: 'comment',
            width: '25%',
            align: 'center',
        }];
    }

    handleDirectionChange = (value) => {
        this.setState({
            direction: value,
        });
        console.log("direction:",value)
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
        this.setState({
            data:[],
            count:0,
        });
        let type='';
        let line_name='';
        let isWorkday = this.state.isWorkday;
        let isHoliday = this.state.isHoliday;
        if (this.state.direction === ''){
            alert("请选择线路方向");
            return;
        }
        if (isHoliday === ''){
            alert("请选择是否是寒暑假");
            return;
        }
        if (isWorkday === ''){
            alert("请选择是否是工作日");
            return;
        }

        else if (this.state.direction==='顺时针')
            line_name = 'LoopLineClockwise';
        else
            line_name = 'LoopLineAntiClockwise';
        if ((isWorkday === 'true') && (isHoliday ==='true'))
            type = 'HolidayWorkday';
        else if ((isWorkday === 'true') && (isHoliday ==='false'))
            type = 'NormalWorkday';
        else if ((isWorkday === 'false') && (isHoliday==='true'))
            type = 'HolidayWeekend';
        else if ((isWorkday === 'false') && (isHoliday === 'false'))
            type = 'NormalWeekendAndLegalHoliday';
        let temproute = 'line_name='+line_name+'&type='+type;
        console.log("temproute:",temproute);
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
                        for (var i=0; i < len; i++) {
                            const {data,count}=this.state;
                            const add = {
                                "key": this.state.count+1,
                                "shiftid": result.schedule.scheduleShift[i],
                                "startTime": result.schedule.scheduleTime[i],
                                "comment":result.schedule.scheduleComment[i],
                                "direction":this.state.direction,
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
        const directionOptions = directionData.map(direction => <Option key={direction}>{direction}</Option>);
        return(
            <Layout>
                {context.header('3')}
                <Content style={{ marginLeft:'3%', marginRight:'3%' }}>
                    {context.breadcrumb("信息查询","查询校内巴士时刻表")}
                    <Layout style={{ padding: '1.5% 1%', background: '#fff' }}>
                        {context.sider_search("sub2","3")}
                        <Content style={{ padding: '1% 1.5%', minHeight: 280 }}>
                            <br />
                            <Select defaultValue="请选择方向" size="large" style={{marginLeft:'19%', width:'13%'}} onChange={this.handleDirectionChange}>
                                {directionOptions}
                            </Select>
                            <Select defaultValue="是否寒暑假" size="large" style={{marginLeft:'3%', width:'13%'}} onChange={this.handleHolidayChange}>
                                <Option value="true">寒暑假</Option>
                                <Option value="false">非寒暑假</Option>
                            </Select>
                            <Select defaultValue="是否工作日" size="large" style={{marginLeft:'3%', width:'13%'}} onChange={this.handleWorkdayChange}>
                                <Option value="true">工作日</Option>
                                <Option value="false">双休日</Option>
                            </Select>
                            <Button type="primary"  size="large" style={{width: '10%', marginLeft: '3.5%'}} icon="search" onClick = {this.handleSearch}>搜索</Button>
                            <h1 />
                            <br />
                            <Table style={{width:'88%', marginLeft:'6%'}} columns={this.columns} dataSource={this.state.data} />
                        </Content>
                    </Layout>
                </Content>
                {context.footer}
            </Layout>
        );
    }

}

export default SearchInShift;
