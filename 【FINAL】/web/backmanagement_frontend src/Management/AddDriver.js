import { Layout, Input, Select, Button} from 'antd';
import React, { Component } from 'react';
import './../App.css';
import context from "../context";

const {Content} = Layout;
const Option = Select.Option;

class AddDriver extends React.Component {
    constructor(props){
        super(props);
        this.state={
            id:'',
            username:'',
            password:'',
            passwordConfirm:'',
            phoneNumber:'',
            identity:'',
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

    handleSelect = (e) =>{
        this.setState({
            identity:e
        })

    };

    handleAdd = (e) => {
        e.preventDefault();
        let username = this.state.username;
        let password = this.state.password;
        let passwordConfirm = this.state.passwordConfirm;
        let identity = this.state.identity;
        let phoneNumber = this.state.phoneNumber;
        if (username.length === 0) {
            alert("用户名不能为空");
            return;
        }
        else if (password.length === 0) {
            alert("密码不能为空");
            return;
        }
        else if (passwordConfirm.length === 0) {
            alert("密码不能为空");
            return;
        }
        else if (phoneNumber.length === 0) {
            alert("电话号码不能为空");
            return;
        }
        else if (identity.length === 0) {
            alert("还未选择用户身份");
            return;
        }
        else if (password !== passwordConfirm) {
            alert("两次输入密码错误");
            return;
        }
        else if (phoneNumber.length !== 11){
            alert("联系电话长度应为11位");
            return;
        }

        console.log("username:",username);
        console.log("password:",password);
        console.log("phone:",phoneNumber);
        console.log("identity:",identity);
        fetch(context.api+'/driver/add?username='+ username + '&password='+ password + '&phone='+ phoneNumber,
            {
                method: 'POST',
                mode: 'cors',
            })
            .then(response => {
                console.log('Request successful', response);
                return response.json()
                    .then(result => {
                        console.log("result:",result);
                        console.log("result:",result.msg);
                        if (result.msg === "success") {
                            alert("新司机添加成功");
                            window.location.reload();
                        }
                        else if(result.msg === "existed"){
                            alert("该司机已经存在");
                            window.location.reload();
                        }
                        else{
                            alert("司机添加失败");
                        }
                    })
            });
    };


    render(){
        return(
            <Layout>
                {context.header('2')}
                <Content style={{ marginLeft:'3%', marginRight:'3%'}}>
                    {context.breadcrumb("信息管理","添加司机")}
                    <Layout style={{ padding: '1.5% 1%', background: '#fff' }}>
                        {context.sider_management("sub3","9")}
                        <Content>
                            <h1 />
                            <h2 style={{marginLeft:'42.5%', fontWeight:"bold"}}>添加新司机</h2>
                            <br/>
                            <h6/>
                            <span style={{marginLeft:"29%", fontSize:'120%'}}> 姓名： </span>
                            <Input name="username" label="用户名" size="large" style={{width: '30%' }}
                                   placeholder="请输入用户名" onChange={this.handleChange}/>
                            <h1/>
                            <span style={{marginLeft: "26%", fontSize:'120%'}}> 用户密码： </span>
                            <Input name="password" label="密码" size="large" style={{width: '30%',}} placeholder="请输入密码"
                                   onChange={this.handleChange} onKeyDown={this.handleKeyDown}/>
                            <h1/>
                            <span style={{marginLeft: '26%', fontSize:'120%'}}> 确认密码： </span>
                            <Input name="passwordConfirm" size="large" style={{width: '30%', }} placeholder="请再次输入密码"
                                   onChange={this.handleChange} onKeyDown={this.handleKeyDown}/>
                            <h1/>
                            <span style={{marginLeft: '26%', fontSize:'120%'}}> 电话号码： </span>
                            <Input name="phoneNumber" lable="电话" size="large" style={{width: '30%',}} placeholder="请输入电话号码"
                                   onChange={this.handleChange} />
                            <h1/>
                            <span style={{marginLeft: '26%', fontSize:'120%'}}> 用户身份： </span>
                            <Select  name="identity" defaultValue="选择身份" size="large" style={{width: '20%'}} onSelect={this.handleSelect}>
                                <Option value="校内巴士司机">校内巴士司机</Option>
                                <Option value="校区巴士司机">校区巴士司机</Option>
                            </Select>
                            <h1/>
                            <br/>
                            <Button type="primary"  size="large" style={{width: '15%', marginLeft: '40%'}} onClick = {this.handleAdd}>添加司机</Button>
                        </Content>
                    </Layout>
                </Content>
                {context.footer}
            </Layout>
        );
    }

}

export default AddDriver;
