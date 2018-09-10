
import { Layout, Icon, Input, InputNumber, Button, Popconfirm, Form, Table} from 'antd';
import React, { Component } from 'react';
import './../App.css';
import context from "../context";

const { Content} = Layout;

const FormItem = Form.Item;
const EditableContext = React.createContext();

const EditableRow = ({ form, index, ...props }) => (
    <EditableContext.Provider value={form}>
        <tr {...props} />
    </EditableContext.Provider>
);

const EditableFormRow = Form.create()(EditableRow);

class EditableCell extends React.Component {
    getInput = () => {
        if (this.props.inputType === 'number') {
            return <InputNumber />;
        }
        return <Input />;
    };

    render() {
        const {
            editing,
            dataIndex,
            title,
            inputType,
            record,
            index,
            ...restProps
        } = this.props;
        return (
            <EditableContext.Consumer>
                {(form) => {
                    const { getFieldDecorator } = form;
                    return (
                        <td {...restProps}>
                            {editing ? (
                                <FormItem style={{ margin: 0 }}>
                                    {getFieldDecorator(dataIndex, {
                                        initialValue: record[dataIndex],
                                    })(this.getInput())}
                                </FormItem>
                            ) : restProps.children}
                        </td>
                    );
                }}
            </EditableContext.Consumer>
        );
    }
}


class ModifyDriver extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data:[],
            editingKey: '',
            content:'',
            count:0,
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
                editable: true,
                align: 'center',
            }, {
                title: '电话号码',
                dataIndex: 'phone',
                key: 'phone',
                width: '28%',
                editable: true,
                align: 'center',
            }, {
                title: '操作',
                dataIndex: 'operation',
                align: 'center',
                render: (text, record) => {
                    const editable = this.isEditing(record);
                    return (
                        <div>
                            {editable ? (
                                <span>
                  <EditableContext.Consumer>
                    {form => (
                        <a
                            href="javascript:;"
                            onClick={() => this.save(form, record.key)}
                            style={{ marginRight: 20 }}
                        >
                            保存
                        </a>
                    )}
                  </EditableContext.Consumer>
                  <Popconfirm
                      title="确定取消?"
                      onConfirm={() => this.cancel(record.key)}
                  >
                    <a>取消</a>
                  </Popconfirm>
                </span>
                            ) : (
                                <a onClick={() => this.edit(record.key)}>编辑</a>
                            )}
                        </div>
                    );
                },
            },
        ];
    }

    isEditing = (record) => {
        return record.key === this.state.editingKey;
    };

    edit(key) {
        this.setState({ editingKey: key });
    }

    save(form, key) {
        form.validateFields((error, row) => {
            if (error) {
                return;
            }
            const newData = [...this.state.data];
            const index = newData.findIndex(item => key === item.key);
            if (index > -1) {
                const old_item = newData[index];
                //console.log("item1:",newData[index]['ID']);
                newData.splice(index, 1, {
                    ...old_item,
                    ...row,
                });
                const new_item = newData[index];
                //console.log("item2:",newData[index]['ID']);
                //console.log("data:",JSON.stringify(newData));

                /*if (new_item['ID'] !== old_item['ID']){
                    new_item['ID'] = old_item['ID'];
                    alert("不能修改司机的ID");
                    return;
                }*/
                if (new_item['name'] === ''){
                    alert("司机用户名不能为空");
                    return;
                }
                if (new_item['phone'] === ''){
                    alert("联系电话不能为空");
                    return;
                }
                console.log("username:", new_item['name']);
                console.log("phone:", new_item['phone']);
                this.setState({ data: newData, editingKey: '' });
                fetch(context.api+'/driver/modify?driverId='+ new_item['ID']+ '&username=' + new_item['name'] + '&phone=' + new_item['phone'] ,
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
                                    alert("修改成功");
                                }
                                else {
                                    alert("修改失败");
                                }
                            })
                    });

            } else {
                newData.push(this.state.data);
                this.setState({ data: newData, editingKey: '' });
            }
        });
    }

    cancel = () => {
        this.setState({ editingKey: '' });
    };

    onChangeContent = (e) => {
        this.setState({
            content: e.target.value,
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
        const components = {
            body: {
                row: EditableFormRow,
                cell: EditableCell,
            },
        };

        const columns = this.columns.map((col) => {
            if (!col.editable) {
                return col;
            }
            return {
                ...col,
                onCell: record => ({
                    record,
                    inputType: col.dataIndex === 'age' ? 'number' : 'text',
                    dataIndex: col.dataIndex,
                    title: col.title,
                    editing: this.isEditing(record),
                }),
            };
        });

        return(
            <Layout>
                {context.header('2')}
                <Content style={{ marginLeft:'3%', marginRight:'3%' }}>
                    {context.breadcrumb("信息管理","修改司机信息")}
                    <Layout style={{ padding: '1.5% 1%', background: '#fff' }}>
                        {context.sider_management("sub3","11")}
                        <Content>
                            <br/>
                            <Input name="content" label="搜索内容" size="large" style={{width: '30%', marginLeft:'30%' }}
                                   prefix={<Icon type="search"/>} placeholder="请输入用户相关信息" onChange={this.onChangeContent}/>
                            <Button type="primary"  size="large" style={{width: '10%', marginLeft: '1%'}} onClick = {this.handleSearch}>搜索</Button>
                            <h6 />
                            <br/>
                            <Table
                                components={components}
                                bordered
                                dataSource={this.state.data}
                                columns={columns}
                                rowClassName="editable-row"
                                style={{width:'60%', marginLeft:'20%'}}
                            />
                        </Content>
                    </Layout>
                </Content>
                {context.footer}
            </Layout>
        );
    }

}

export default ModifyDriver;
