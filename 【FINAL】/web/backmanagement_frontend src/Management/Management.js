import { Layout} from 'antd';
import React, { Component } from 'react';
import './../App.css';
import context from "../context";

const {  Content } = Layout;

class Management extends React.Component {
    constructor(props){
        super(props);
        /*fetch('http://localhost:8080/administrator/judgestate',
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
                        if (result.msg === "not logged") {
                            alert("请先登录");
                            window.location.href = "/adminLogin"
                        }
                        else if (result.msg === "fail"){
                            alert("您暂时不可使用该功能");
                            this.props.history.push("/login");
                        }
                    })
            });*/
    }

    render(){
        return(
            <Layout>
                {context.header('2')}
                <Content style={{ marginLeft:'3%', marginRight:'3%' }}>
                    {context.breadcrumb("信息管理","")}
                    <Layout style={{ padding: '24px 0', background: '#fff' }}>
                        {context.sider_management("","")}
                        <Content></Content>
                    </Layout>
                </Content>
                {context.footer}
            </Layout>
        );
    }

}

export default Management;/**
 * Created by 励颖 on 2018/7/2.
 */
