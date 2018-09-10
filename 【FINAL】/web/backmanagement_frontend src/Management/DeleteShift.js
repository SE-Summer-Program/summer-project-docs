import { Layout, Icon, Input, Button, Popconfirm, Table} from 'antd';
import React, { Component } from 'react';
import './../App.css';
import context from "../context";

const {Content} = Layout;

class DeleteShift extends React.Component {
    constructor(props){
        super(props);
        this.state={
            data:[],
            count:0,
            content:'',
        };
        this.columns = [{
            title: '班次编号',
            dataIndex: 'shiftid',
            key: 'shiftid',
            width: '15%',
            align: 'center',
        }, {
            title: '线路名',
            dataIndex: 'lineName',
            key: 'lineName',
            width: '18%',
            align: 'center',
        },{
            title: '运行时间',
            dataIndex: 'type',
            key: 'type',
            width: '15%',
            align: 'center',
        },  {
            title: '出发时刻',
            dataIndex: 'startTime',
            key: 'startTime',
            width: '13%',
            align: 'center',
        }, {
            title: '预留座位数',
            dataIndex: 'seat' ,
            key: 'seat',
            width: '12%',
            align: 'center',
        }, {
            title: '备注',
            dataIndex: 'comment' ,
            key: 'comment',
            width: '20%',
            align: 'center',
        }, {
            title: '删除',
            dataIndex: 'operation',
            align: 'center',
            render: (text, record) => {
                return (
                    <Popconfirm title="确定删除?" onConfirm={() => this.onDelete(record.key)}>
                        <a href="javascript:"><Icon type="delete"/></a>
                    </Popconfirm>)
            }
        }];
    }

   /* onDelete = (key) => {
        const data = [...this.state.data];
        console.log("delete:",data[key].shiftid);
        fetch(context.api+'/shift/delete?shiftId='+ data[key-1].shiftid,
            {
                method: 'POST',
                mode: 'cors',
            })
            .then(response => {
                console.log('Request successful', response);
                return response.json()
                    .then(result => {
                        if (result.msg === "success") {
                            this.setState({data: data.filter(item => item.key !== key)});
                            alert("删除成功");
                        }
                        else {
                            alert("删除失败");
                        }
                        for(let i = 0; i<this.state.data.length; i++){
                            console.log("key:", this.state.data[i].key);
                        }
                    })
            });


    };*/

    onDelete = (key) => {
        console.log("delete:",key,this.state.data[key].shiftid);
        fetch('http://localhost:8080/shift/delete?shiftId='+ this.state.data[key].shiftid,
            {
                method: 'POST',
                mode: 'cors',
            })
            .then(response => {
                console.log('Request successful', response);
                return response.json()
                    .then(result => {
                        if (result.msg === "success") {
                            alert("删除成功");
                            fetch('http://localhost:8080/shift/search_shift?content='+this.state.content,
                                {
                                    method: 'GET',
                                    mode: 'cors',
                                })
                                .then(response => {
                                    console.log('Request successful', response);
                                    return response.json()
                                        .then(result => {
                                            let len = result.shiftList.length;
                                            console.log("response len:",len);
                                            this.setState({
                                                data:[],
                                                count:0,
                                            });
                                            for (let i=0; i < len; i++) {
                                                const {data,count}=this.state;
                                                let shift = result.shiftList[i];
                                                let type = shift.lineType;
                                                let typeName = "";
                                                if (type === "HolidayWorkday") {
                                                    typeName = "寒暑假工作日";
                                                }
                                                else if (type === "NormalWorkday") {
                                                    typeName = "普通工作日";
                                                }
                                                else if (type === "HolidayWeekend")
                                                    typeName = "寒暑假双休日";
                                                else{
                                                    typeName = "普通节假双休日"
                                                }
                                                const add = {
                                                    "key": count,
                                                    "shiftid": shift.shiftId,
                                                    "startTime": shift.departureTime,
                                                    "comment": shift.comment,
                                                    "lineName": shift.lineNameCn,
                                                    "seat": shift.reserveSeat,
                                                    "type": typeName,
                                                };

                                                this.setState({
                                                    data: [...data, add],
                                                    count: count+1,
                                                });
                                            }
                                        })
                                });
                        }
                        else {
                            alert("删除失败");
                        }
                    })
            });
    };

    onChangeContent = (e) => {
        this.setState({
            content: e.target.value,
        })
    };

    handleSearch = () => {
        console.log("content:",this.state.content);
        this.state.data=[];
        fetch(context.api+'/shift/search_shift?content='+this.state.content,
            {
                method: 'GET',
                mode: 'cors',
            })
            .then(response => {
                console.log('Request successful', response);
                return response.json()
                    .then(result => {
                        let len = result.shiftList.length;
                        console.log("response len:",len);
                        this.setState({
                            data:[],
                            count:0,
                        });
                        for (let i=0; i < len; i++) {
                            const {data,count}=this.state;
                            let shift = result.shiftList[i];
                            let type = shift.lineType;
                            let typeName = "";
                            if (type === "HolidayWorkday") {
                                typeName = "寒暑假工作日";
                            }
                            else if (type === "NormalWorkday") {
                                typeName = "普通工作日";
                            }
                            else if (type === "HolidayWeekend")
                                typeName = "寒暑假双休日";
                            else{
                                typeName = "普通节假双休日"
                            }
                            const add = {
                                "key": count,
                                "shiftid": shift.shiftId,
                                "startTime": shift.departureTime,
                                "comment": shift.comment,
                                "lineName": shift.lineNameCn,
                                "seat": shift.reserveSeat,
                                "type": typeName,
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
        return(
            <Layout>
                {context.header('2')}
                <Content style={{ marginLeft:'3%', marginRight:'3%' }}>
                    {context.breadcrumb("信息管理","删除班次")}
                    <Layout style={{ padding: '1.5% 1%', background: '#fff' }}>
                        {context.sider_management("sub2","6")}
                        <Content>
                            <br />
                            <Input name="content" label="搜索内容" size="large" style={{width: '30%', marginLeft:'30%' }}
                                   prefix={<Icon type="search"/>} placeholder="请输入车次相关信息" onChange={this.onChangeContent}/>
                            <Button type="primary"  size="large" style={{width: '10%', marginLeft: '1%'}} onClick = {this.handleSearch}>搜索</Button>
                            <h6 />
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

export default DeleteShift;
