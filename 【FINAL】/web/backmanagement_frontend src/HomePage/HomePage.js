import { Layout, Menu, Breadcrumb, Icon } from 'antd';
import React, { Component } from 'react';
import './../App.css';
import pic from "./../bus_background.jpg";
import context from "../context";

const { SubMenu } = Menu;
const { Header, Content, Footer,} = Layout;

class HomePage extends React.Component {
    render(){
        return(
            <Layout>
                {context.header('1')}
                <Content style={{ marginLeft:'3%', marginRight:'3%'}}>
                    <Breadcrumb style={{ margin: "1.5%" }}>
                        <Breadcrumb.Item>校园巴士主页</Breadcrumb.Item>
                    </Breadcrumb>
                    <Layout style={{ padding: '1%', background: '#fff' }}>

                            <img src={pic} width = "100%" height= "100%"/>

                    </Layout>
                </Content>
                {context.footer}
            </Layout>
        );
    }

}

export default HomePage;