import { Layout, Icon, Input, Button, Popconfirm, Table, } from 'antd';
import React, { Component } from 'react';
import './../App.css';

import context from "../context";

const {Content} = Layout;

class DeleteDriver extends React.Component {
    constructor(props){
        super(props);
        this.state={
            data:[],
            count:1,
            content:''
        };
        this.columns = [{
            title: '司机ID',
            dataIndex: 'ID',
            key: 'ID',
            width: '20%',
            align: 'center',
        }, {
            title: '姓名',
            dataIndex: 'name',
            key: 'name',
            width: '28%',
            align: 'center',
        }, {
            title: '电话号码',
            dataIndex: 'phone',
            key: 'phone',
            width: '28%',
            align: 'center',
        },  {
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

    onDelete = (key) => {
        const data = [...this.state.data];
        console.log("delete:",data[key].ID);
        fetch(context.api+'/driver/delete?driverId='+ data[key].ID,
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
                    })
            });
    };

    onChangeContent = (e) => {
        this.setState({
            content:e.target.value,
        })
    };

    handleSearch = () => {
        console.log("content:",this.state.content);
        fetch(context.api+'/driver/search?content='+this.state.content,
            {
                method: 'GET',
                mode: 'cors',
            })
            .then(response => {
                console.log('Request successful', response);
                return response.json()
                    .then(result => {
                        if (result.msg === "success"){
                            let len = result.driverList.length;
                            console.log("result:", result);
                            console.log("response len:",len);
                            this.setState({
                                data:[],
                                count:0,
                            });
                            for (let i=0; i < len; i++) {
                                const {data,count}=this.state;
                                let driver = result.driverList[i];
                                const add = {
                                    "key": this.state.count,
                                    "ID": driver.driverId,
                                    "name": driver.username,
                                    "phone": driver.phone,
                                };
                                this.setState({
                                    data: [...data, add],
                                    count: count+1,
                                });
                            }
                            this.setState({
                                content:'',
                            })
                        }
                        else
                        {
                            alert("查询失败，请重新搜索");
                            window.location.reload();
                        }
                    })
            });
    };

    render(){
        return(
            <Layout>
                {context.header('2')}
                <Content style={{ marginLeft:'3%', marginRight:'3%' }}>
                    {context.breadcrumb("信息管理","删除司机")}
                    <Layout style={{ padding: '1.5% 1%', background: '#fff' }}>
                        {context.sider_management("sub3","10")}
                        <Content>
                            <br />
                            <Input name="content" label="搜索内容" size="large" style={{width: '30%', marginLeft:'30%' }}
                                   prefix={<Icon type="search"/>} placeholder="请输入司机相关信息" onChange={this.onChangeContent}/>
                            <Button type="primary"  size="large" style={{width: '10%', marginLeft: '1%'}} onClick = {this.handleSearch}>搜索</Button>
                            <h6 />
                            <br/>
                            <Table style={{width:'60%', marginLeft:'20%'}} columns={this.columns} dataSource={this.state.data} />
                        </Content>
                    </Layout>
                </Content>
                {context.footer}
            </Layout>
        );
    }

}

export default DeleteDriver;
