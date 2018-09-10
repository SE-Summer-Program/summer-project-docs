import { Layout} from 'antd';
import React, { Component } from 'react';
import './../App.css';
import context from "../context";

const {Content} = Layout;

class Search extends React.Component {
    constructor(props){
        super(props);
        /*fetch(context.api+'/administrator/judgestate',
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
                {context.header('3')}
                <Content style={{ marginLeft:'3%', marginRight:'3%' }}>
                    {context.breadcrumb("信息查询","")}
                    <Layout style={{ padding: '1.5% 1%', background: '#fff' }}>
                        {context.sider_search("","")}
                    </Layout>
                </Content>
                {context.footer}
            </Layout>
        );
    }

}

export default Search;
