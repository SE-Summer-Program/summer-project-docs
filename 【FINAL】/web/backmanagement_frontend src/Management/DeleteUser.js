import { Layout, Menu, Breadcrumb, Icon, Input, Select, Button, Popconfirm, Table, } from 'antd';
import React, { Component } from 'react';
import './../App.css';
import {Link} from "react-router-dom";
import context from "../context";

const {Content} = Layout;
const Option = Select.Option;

class DeleteUser extends React.Component {
    constructor(props){
        super(props);
        this.state= {
            data: [],
            count: 0,
            content: ''
        };
        this.columns = [{
            title: '姓名',
            dataIndex: 'name',
            key: 'name',
            width: '15%',
            align: 'center',
        }, {
            title: 'ID',
            dataIndex: 'ID',
            key: 'ID',
            width: '20%',
            align: 'center',
        }, {
            title: '电话号码',
            dataIndex: 'phone',
            key: 'phone',
            width: '20%',
            align: 'center',
        }, {
            title: '积分',
            dataIndex: 'credit' ,
            key: 'credit',
            width: '15%',
            align: 'center',
        }, {
            title: '身份',
            dataIndex: 'identity' ,
            key: 'identity',
            width: '18%',
            align: 'center',
        },{
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
        fetch(context.api+'/user/delete?userId='+ data[key-1].ID,
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
        this.state.data=[];
        console.log(context.api+'/user/search?content='+this.state.content);
        fetch(context.api+'/user/search?content='+this.state.content,
            {
                method: 'POST',
                mode: 'cors',
            })
            .then(response => {
                //console.log('Request successful', response);
                return response.json()
                    .then(result => {
                        if (result.msg === 'success'){
                            let len = result.userList.length;
                            this.setState({
                                data:[],
                                count:0,
                            });
                            for (let i=0; i < len; i++) {
                                const {data,count}=this.state;
                                let user = result.userList[i];
                                let identity = '';
                                if (user.teacher.toString() === 'false') {
                                    identity = "学生";
                                }
                                else{
                                    identity = "教师"
                                }
                                const add = {
                                    "key": this.state.count+1,
                                    "ID": user.userId,
                                    "name": user.username,
                                    "credit": user.credit,
                                    "identity": identity,
                                    "phone": user.phone,
                                };
                                this.setState({
                                    data: [...data, add],
                                    count: count+1,
                                });
                            }
                        }
                        else{
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
                    {context.breadcrumb("信息管理","删除用户")}
                    <Layout style={{ padding: '1.5% 1%', background: '#fff' }}>
                        {context.sider_management("sub1","2")}
                        <Content>
                            <br />
                            <Input name="content" label="搜索内容" size="large" style={{width: '30%', marginLeft:'27%' }}
                                   prefix={<Icon type="search"/>} placeholder="请输入用户相关信息" onChange={this.onChangeContent}/>
                            <Button type="primary"  size="large" style={{width: '10%', marginLeft: '1%'}} onClick = {this.handleSearch}>搜索</Button>
                            <h6 />
                            <br />
                            <Table style={{width:'88%', marginLeft:'5%'}} columns={this.columns} dataSource={this.state.data} />
                        </Content>
                    </Layout>
                </Content>
                {context.footer}
            </Layout>
        );
    }

}

export default DeleteUser;
