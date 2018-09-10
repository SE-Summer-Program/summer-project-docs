/**
 * Created by 励颖 on 2018/7/2.
 */
/**
 * Created by 励颖 on 2018/4/3.
 */
import React, { Component } from 'react';
import { Button, Layout, Menu, Breadcrumb, Icon,  Form, Input, Tooltip, Cascader, Select, Row, Col, Checkbox, AutoComplete} from 'antd';
import {Link } from "react-router-dom";
import context from "../context";


const { Header, Content, Footer } = Layout;
const FormItem = Form.Item;
const Option = Select.Option;
const AutoCompleteOption = AutoComplete.Option;

class RegistrationForm extends React.Component {
    constructor(props){
        super(props);
        this.state={
            username:'',
            password:'',
            passwordConfirm:'',
            checked: false,
        }
    }

    handleChange = (e) => {
        this.setState({[e.target.name]:e.target.value})
    };

    handleKeyDown = (e) => {
        if (e.keyCode === 16){
            this.handleSubmit(e)
        }
    };

    toggleChecked = () => {
        this.setState({
            checked: !this.state.checked
        })
    }

    handleRegister = (e) => {
        e.preventDefault();
        let username = this.state.username;
        let password = this.state.password;
        let passwordConfirm = this.state.passwordConfirm;
        if (username.length === 0) {
            alert("用户名不能为空");
        }
        else if (password.length === 0) {
            alert("密码不能为空");
        }
        else if (passwordConfirm.length === 0) {
            alert("密码不能为空");
        }
        else if (password !== passwordConfirm) {
            alert("两次输入密码错误");
            window.location.reload();
        }
        else if (!this.state.checked)
            alert("请先勾选同意协议");
        console.log("username:",username);
        console.log("password:",password);
        fetch('http://localhost:8080/administrator/register?username=' + username + '&password=' + password,
            {
                method: 'POST',
                mode: 'cors',
                credentials: 'include',
            })
            .then(response => {
                console.log('Request successful', response);
                return response.json()
                    .then(result => {
                        console.log("result:", result);
                        if (result.msg === "success") {
                            alert("注册并登录成功");
                            window.location.href="http://localhost:3000/#/"
                        }
                        else if (result.msg === "fail") {
                            alert("注册失败");
                        }
                        else if (result.msg === "existed"){
                            alert("用户名已存在");
                        }
                    })
            });

    };

    render()
    {
        const tailFormItemLayout = {
            wrapperCol: {
                xs: {
                    span: 24,
                    offset: 0,
                },
                sm: {
                    span: 16,
                    offset: 8,
                },
            },
        };
        return (
            <Layout>
                <Header className="header">
                    <div className="logo" />
                    <Menu
                        theme="dark"
                        mode="horizontal"
                        defaultSelectedKeys={['5']}
                        style={{ lineHeight: '64px' }}
                    >
                        <Menu.Item key="1"><Link to="./"><span><Icon type="home"/></span>主页</Link></Menu.Item>
                        <Menu.Item key="2"><Link to="management"><span><Icon type="setting"/></span>管理信息</Link></Menu.Item>
                        <Menu.Item key="3"><Link to="search"><span><Icon type="search"/></span>查询信息</Link></Menu.Item>
                        <Menu.Item key="4"><Link to="statistics"><span><Icon type="form"/></span>统计信息</Link></Menu.Item>
                        <Menu.Item key="5"><Link to="login"><span><Icon type="user"/></span>登录</Link></Menu.Item>
                    </Menu>
                </Header>
                <Content style={{padding: '0 50px'}}>
                    <Breadcrumb style={{margin: '16px 0'}}>
                        <Breadcrumb.Item>主页</Breadcrumb.Item>
                        <Breadcrumb.Item>注册</Breadcrumb.Item>
                    </Breadcrumb>
                    <Layout style={{padding: '24px 0', background: '#fff'}}>

                        <Content style={{padding: '0 24px', minHeight: 280}}>
                            <Form onSubmit={this.handleSubmit}>
                                <br/>
                                <h2 style={{marginLeft:'580px'}}>管理员注册</h2>
                                <br/>
                                <span style={{marginLeft: '462px', fontSize:'18px'}}> 姓名： </span>
                                <Input name="username" label="用户名" size="large" style={{width: '23%', }}
                                       placeholder="请输入用户名" onChange={this.handleChange}/>

                                <h1/>
                                <span style={{marginLeft: '430px', fontSize:'18px'}}> 用户密码： </span>
                                <Input name="password" label="密码" size="large" style={{width: '23%',}} placeholder="请输入密码"
                                       onChange={this.handleChange} onKeyDown={this.handleKeyDown}/>
                                <h1/>
                                <span style={{marginLeft: '430px', fontSize:'18px'}}> 确认密码： </span>
                                <Input name="passwordConfirm" size="large" style={{width: '23%', }} placeholder="请再次输入密码"
                                       onChange={this.handleChange} onKeyDown={this.handleKeyDown}/>
                                <h1/>
                                <span style={{marginLeft: '445px', fontSize:'18px'}}> 验证码： </span>
                                <Input name="phoneNumber" lable="电话" size="large" style={{width: '11%',}} placeholder="请输入验证码"
                                       onChange={this.handleChange} />
                                &nbsp;
                                <Button size="large">点击获取验证码</Button>
                                <br/>
                                <br/>
                                <Checkbox size="large" style={{marginLeft: '570px', fontSize:'16px'}} onChange={this.toggleChecked}>我同意<a href="">该协议</a></Checkbox>
                                <br/>
                                <br/>
                                <FormItem {...tailFormItemLayout}>
                                    <Button type="primary"  size="large" style={{marginLeft: '190px'}} onClick={this.handleRegister}>注册</Button>
                                </FormItem>
                            </Form>
                        </Content>
                    </Layout>
                </Content>
                {context.footer}
            </Layout>
        );
    }

}

const Register = Form.create()(RegistrationForm);
export default Register;

/**
 * Created by 励颖 on 2018/7/2.
 */
