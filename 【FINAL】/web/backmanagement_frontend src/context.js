import { Layout, Menu, Icon, Breadcrumb, } from 'antd';
import React, { Component } from 'react';
import {Link} from "react-router-dom";

const { Header, Content, Footer, Sider,} = Layout;
const { SubMenu } = Menu;

export default {
    //api:"http://localhost:8080",
    api:"http://106.14.181.49:8080",
    data: {},
    footer:(
        <Footer style={{ textAlign: 'center' }}>
            SJTU BUS BACK STAGE MANAGEMENT SYSTEM
        </Footer>
    ),
    header:function(value){
        return(
            <Header className="header">
            <Menu
                theme="dark"
                mode="horizontal"
                defaultSelectedKeys={[value]}
                style={{ lineHeight: '64px', fontSize:"110%"}}
            >
                <Menu.Item key="1"><Link to="./"><span><Icon type="home"/></span>主页</Link></Menu.Item>
                <Menu.Item key="2"><Link to="management"><span><Icon type="setting"/></span>管理信息</Link></Menu.Item>
                <Menu.Item key="3"><Link to="search"><span><Icon type="search"/></span>查询信息</Link></Menu.Item>
                <Menu.Item key="4"><Link to="statistics"><span><Icon type="form"/></span>统计信息</Link></Menu.Item>
            </Menu>
        </Header>)
    },
    breadcrumb:function (value1, value2){
        return(
            <Breadcrumb style={{ margin: "1.5%", fontWeight:"bold"}}>
                <Breadcrumb.Item>主页</Breadcrumb.Item>
                <Breadcrumb.Item>{value1}</Breadcrumb.Item>
                <Breadcrumb.Item>{value2}</Breadcrumb.Item>
            </Breadcrumb>
        )
    },
    sider_management:function(openKey, selectedKey){
        return(
            <Sider  width="17%" style={{ background: '#fff',}}>
                <Menu
                    mode="inline"
                    defaultOpenKeys={[openKey]}
                    defaultSelectedKeys={[selectedKey]}
                    style={{ height: '100%'}}
                >
                    <SubMenu key="sub1" title={<span><Icon type="user" />普通用户管理</span>}>
                        <Menu.Item key="1"><Link to="adduser">添加用户</Link></Menu.Item>
                        <Menu.Item key="2"><Link to="deleteuser">删除用户</Link></Menu.Item>
                        <Menu.Item key="3"><Link to="modifyuser">修改用户</Link></Menu.Item>
                    </SubMenu>
                    <SubMenu key="sub2" title={<span><Icon type="car" />班次信息管理</span>}>
                        <Menu.Item key="5"><Link to="addshift">添加班次</Link></Menu.Item>
                        <Menu.Item key="6"><Link to="deleteshift">删除班次</Link></Menu.Item>
                        <Menu.Item key="7"><Link to="modifyshift">修改班次</Link></Menu.Item>
                    </SubMenu>
                    <SubMenu key="sub3" title={<span><Icon type="idcard" />司机用户管理</span>}>
                        <Menu.Item key="9"><Link to="adddriver">添加司机</Link></Menu.Item>
                        <Menu.Item key="10"><Link to="deletedriver">删除司机</Link></Menu.Item>
                        <Menu.Item key="11"><Link to="modifydriver">修改司机</Link></Menu.Item>
                    </SubMenu>
                    <SubMenu key="sub4" title={<span><Icon type="form" />公告管理</span>}>
                        <Menu.Item key="12"><Link to="addmessage">发布新公告</Link></Menu.Item>
                    </SubMenu>
                </Menu>
            </Sider>
        )
    },
    sider_search:function(openKey, selectedKey){
        return(
            <Sider width="17%" style={{ background: '#fff' }}>
                <Menu
                    mode="inline"
                    defaultSelectedKeys={[selectedKey]}
                    defaultOpenKeys={[openKey]}
                    style={{ height: '100%'}}
                >
                    <SubMenu key="sub1" title={<span><Icon type="user" />用户信息</span>}>
                        <Menu.Item key="1" ><Link to="searchuser">普通用户</Link></Menu.Item>
                    </SubMenu>
                    <SubMenu key="sub2" title={<span><Icon type="car" />校内巴士</span>}>
                        <Menu.Item key="3"><Link to="searchinshift">始发时刻表</Link></Menu.Item>
                    </SubMenu>
                    <SubMenu key="sub3" title={<span><Icon type="car" />校区巴士</span>}>
                        <Menu.Item key="5"><Link to="searchreserved">预约信息</Link></Menu.Item>
                        <Menu.Item key="6"><Link to="searchoutshift">班次表</Link></Menu.Item>
                    </SubMenu>
                </Menu>
            </Sider>
        )
    },
    sider_statistics:function(selectedKey){
        return(
            <Sider width="17%" style={{ background: '#fff' }}>
                <Menu
                    mode="inline"
                    defaultSelectedKeys={[selectedKey]}
                    style={{ height: '100%' }}
                >
                    <Menu.Item key="1" style={{fontSize:"105%"}}><Link to="userstatistics"><Icon type="user"/>已发车次乘客统计</Link></Menu.Item>
                    <Menu.Item key="2" style={{fontSize:"105%"}}><Link to="appointmentstatistics"><Icon type="table"/>预约信息统计</Link></Menu.Item>
                </Menu>
            </Sider>
        )
    },
    fetch_timedata:function(startStation, endStation, lineType){
        let lineName = startStation + "到" + endStation;
        let timeData=[];
        console.log("route:", this.api+'/shift/search_time?lineNameCn=' + lineName + "&lineType=" + lineType);
        fetch(this.api+'/shift/search_time?lineNameCn=' + lineName + "&lineType=" + lineType,
            {
                method: 'POST',
                mode: 'cors',
            })
            .then(response => {
                console.log('Request successful', response);
                return response.json()
                    .then(result => {
                        let len = result.timeList.length;
                        console.log("response len:", len);
                        for (let i = 0; i < len; i++) {
                            let add = result.timeList[i];
                            timeData.push(add);
                        }
                    });

            });
        console.log("timeData:",timeData);
        return timeData;

    }

}