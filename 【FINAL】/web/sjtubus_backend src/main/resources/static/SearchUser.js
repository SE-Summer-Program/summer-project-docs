import { Layout,Icon, Input, Button, Table } from 'antd';
import React, { Component } from 'react';
import './../App.css';
import context from "../context";

const {Content} = Layout;

class SearchUser extends React.Component {
    constructor(props){
        super(props);
        this.state={
            data:[],
            count:0,
            content:''
        };
        this.columns = [{
            title: '姓名',
            dataIndex: 'name',
            key: 'name',
            width: '18%',
            align: 'center',
        }, {
            title: 'ID',
            dataIndex: 'ID',
            key: 'ID',
            width: '15%',
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
        }];
    }


    onChangeContent = (e) => {
        this.setState({
            content: e.target.value
        })
    };

    handleSearch = () => {
        console.log(this.state.content);
        this.setState({
            data:[],
            count:0,
        });
        console.log(context.api+'/user/search?content='+this.state.content);
        fetch(context.api+'/user/search?content='+this.state.content,
            {
                method: 'POST',
                mode: 'cors',
            })
            .then(response => {
                console.log('Request successful', response);
                return response.json()
                    .then(result => {
                        let len = result.userList.length;
                        console.log("response len:",len);
                        for (let i=0; i < len; i++) {
                            let user = result.userList[i];
                            const {data,count}=this.state;
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
                    })
            });
    };


    render(){
        return(
            <Layout>
                {context.header('3')}
                <Content style={{ marginLeft:'3%', marginRight:'3%' }}>
                    {context.breadcrumb("信息查询","查找用户")}
                    <Layout style={{ padding: '1.5% 1%', background: '#fff' }}>
                        {context.sider_search("sub1","1")}
                        <Content style={{ padding: '1% 1.5%', minHeight: 280 }}>
                            <br />
                            <Input name="content" label="搜索内容" size="large" style={{width: '30%', marginLeft:'28%' }}
                                   prefix={<Icon type="search"/>} placeholder="请输入用户相关信息" onChange={this.onChangeContent}/>
                            <Button type="primary"  size="large" style={{width: '10%', marginLeft: '1%'}} onClick = {this.handleSearch}>搜索</Button>
                            <h6/>
                            <br/>
                            <Table style={{width:'78%', marginLeft:'11%'}} columns={this.columns} dataSource={this.state.data} />
                        </Content>
                    </Layout>
                </Content>
                {context.footer}
            </Layout>
        );
    }
}

export default SearchUser;
