
import { Layout,Input, Button, Radio, DatePicker} from 'antd';
import React, { Component } from 'react';
import './../App.css';
import moment from 'moment';
import context from "../context";

const { Content} = Layout;
const { TextArea } = Input;
const RadioGroup = Radio.Group;
const RangePicker = DatePicker.RangePicker;

class AddMessage extends React.Component {
    constructor(props){
        super(props);
        this.state={
            type:'',
            title:'',
            content:'',
            startDate:'',
            endDate:'',
        }
    }

    handleTypeChange = (e) => {
        this.setState({
            type: e.target.value
        });
    };

    handleTitleChange = (e) => {
        this.setState({
            title: e.target.value
        });
    };

    handleContentChange = (e) => {
        this.setState({
            content: e.target.value
        });
    };

    handleDateChange = (dates, dateStrings) => {
        this.setState({
            startDate: dateStrings[0],
            endDate: dateStrings[1],
        });
        console.log("date1:",dateStrings[0]);
        console.log("date2:",dateStrings[1]);
    };

    handleAdd = () => {
        let messageType = this.state.type;
        let messageTitle = this.state.title;
        let messageContent = this.state.content;
        let startDate = this.state.startDate;
        let endDate = this.state.endDate;
        let title = '';
        if (messageTitle !== "普通公告"){
            title = "【" + messageType + "】";
        }

        let data = "{\"data\": { \"alert\":\""+ messageContent + "\", \"title\": \""+ title + messageTitle + "\",}}";
        //console.log("data:",data);

        let xhr = new XMLHttpRequest();
        xhr.addEventListener("readystatechange", function () {
            if (this.readyState === 4) {
                console.log(this.responseText);
            }
        });
        xhr.open("POST", "https://utfpokja.push.lncld.net/1.1/push");
        xhr.setRequestHeader("x-lc-id", "UTfPOKjAoRvO8m7Gux0964oT-gzGzoHsz");
        xhr.setRequestHeader("x-lc-key", "tnpkj8g2EyCFWydpDrbcXj3X");
        xhr.setRequestHeader("content-type", "application/json");
        xhr.send(data);


        fetch(context.api+'/message/add?messageType=' + messageType + '&messageTitle=' + messageTitle
            + '&messageContent=' + messageContent + '&startDate=' + startDate + '&endDate=' + endDate,
            {
                method: 'POST',
                mode: 'cors',
            })
            .then(response => {
                //console.log('Request successful', response);
                return response.json()
                    .then(result => {
                        console.log("result:",result);
                        console.log("result:",result.msg);
                        if (result.msg === "success") {
                            alert("公告发布成功");
                            //window.location.reload();
                        }
                        else{
                            alert("公告发布失败");
                        }
                    })
            });
    };

    render(){
        return(
            <Layout>
                {context.header('2')}
                <Content style={{ marginLeft:'3%', marginRight:'3%' }}>
                    {context.breadcrumb("信息管理","添加公告")}
                    <Layout style={{ padding: '1.5% 1%', background: '#fff' }}>
                        {context.sider_management("sub4","12")}
                        <Content>
                            <script src="./../bower_components/leancloud-push.js/src/AV.push.js"/>
                            <br/>
                            <h2 style={{marginLeft:'43%', fontWeight:'bold', width:'15%'}}>发布新公告</h2>
                            <h1/>
                            <br/>
                            <span style={{marginLeft: '29%', fontSize:'120%'}}> 公告类型： </span>
                            <RadioGroup onChange={this.handleTypeChange} >
                                <Radio value={"普通公告"}>普通公告</Radio>
                                <Radio value={"紧急通知"}>紧急通知</Radio>
                                <Radio value={"好消息"}>好消息</Radio>
                            </RadioGroup>
                            <h6/>
                            <br/>
                            <span style={{marginLeft: '29%', fontSize:'120%'}}>公告标题： </span>
                            <Input  size="large" style={{width:'30%'}} onChange={this.handleTitleChange}/>
                            <h6/>
                            <br/>
                            <span style={{marginLeft: '29%', fontSize:'120%'}}> 日期范围： </span>
                            <RangePicker size="large" style={{width:'30%'}} onChange={this.handleDateChange} ranges={{ '今天': [moment(), moment()], '本月': [moment(), moment().endOf('month')] }}/>
                            <h6/>
                            <br/>
                            <span style={{marginLeft: '29%', fontSize:'120%'}}> 公告内容： </span>
                            <TextArea size="large" style={{width:'30%'}} autosize={{ minRows: 4, maxRows: 8 }} onChange={this.handleContentChange}/>
                            <h1/>
                            <br/>
                            <Button type="primary"  size="large" style={{width: '15%', marginLeft: '40%'}} onClick = {this.handleAdd}>发布</Button>
                        </Content>
                    </Layout>
                </Content>
                {context.footer}
            </Layout>
        );
    }
}

export default AddMessage;
