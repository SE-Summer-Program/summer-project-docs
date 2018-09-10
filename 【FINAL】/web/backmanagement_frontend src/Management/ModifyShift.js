
import { Layout, Icon, Input, InputNumber, Button, Popconfirm, Form, Table} from 'antd';
import React, { Component } from 'react';
import './../App.css';
import context from "../context";

const { Content} = Layout;
const format = 'HH:mm';

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
            return <InputNumber min={0} max={50}/>;
        }
        return <Input />
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


class ModifyShift extends React.Component {
    constructor(props){
        super(props);
        this.state={
            data:[],
            count:0,
            editingKey: '',
            content:'',
        };
        this.columns = [{
            title: '班次编号',
            dataIndex: 'shiftid',
            key: 'shiftid',
            width: '15%',
            align: 'center',
        }, {
            title: '线路方向',
            dataIndex: 'lineNameCn',
            key: 'lineNameCn',
            width: '18%',
            align: 'center',
        }, {
            title: '时段类型',
            dataIndex: 'lineType',
            key: 'lineType',
            width: '18%',
            align: 'center',
        },   {
            title: '出发时刻',
            dataIndex: 'departureTime',
            key: 'departureTime',
            width: '18%',
            align: 'center',
        }, {
            title: '预留座位数',
            dataIndex: 'seat' ,
            key: 'seat',
            width: '15%',
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
        },];
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

                console.log("username:", new_item['shiftid']);
                console.log("phone:", new_item['seat']);

                this.setState({ data: newData, editingKey: '' });
                fetch(context.api+'/shift/modify?shiftId='+ new_item['shiftid']+ '&reserveSeat=' + new_item['seat'],
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
        fetch(context.api+'/shift/search_shift?content='+this.state.content,
            {
                method: 'GET',
                mode: 'cors',
            })
            .then(response => {
                console.log('Request successful', response);
                return response.json()
                    .then(result => {
                        if (result.msg === "success"){
                            let len = result.shiftList.length;
                            console.log("result:", result);
                            console.log("response len:",len);
                            this.setState({
                                data:[],
                                count:0,
                            });
                            for (let i=0; i < len; i++) {
                                const {data,count}=this.state;
                                let shift = result.shiftList[i];
                                let type = shift.lineType;
                                let typeName = "";
                                if (type === "HolidayWorkday") {
                                    typeName = "寒暑假工作日";
                                }
                                else if (type === "NormalWorkday") {
                                    typeName = "普通工作日";
                                }
                                else if (type === "HolidayWeekend")
                                    typeName = "寒暑假双休日";
                                else{
                                    typeName = "普通节假双休日"
                                }
                                const add = {
                                    "key": this.state.count+1,
                                    "shiftid": shift.shiftId,
                                    "departureTime": shift.departureTime,
                                    "comment": shift.comment,
                                    "lineNameCn": shift.lineNameCn,
                                    "seat": shift.reserveSeat,
                                    "lineType": typeName,
                                };
                                this.setState({
                                    data: [...data, add],
                                    count: count+1,
                                });
                            };
                            /*this.setState({
                                content:'',
                            })*/
                        }
                        else
                        {
                            alert("查询失败，请重新搜索");
                            this.setState({
                                content:'',
                            })
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
                    inputType: col.dataIndex === 'seat' ? 'number' : 'text',
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
                    {context.breadcrumb("信息管理","修改班次信息")}
                    <Layout style={{ padding: '1.5% 1%', background: '#fff' }}>
                        {context.sider_management("sub2","7")}
                        <Content>
                            <br />
                            <Input name="content" label="搜索内容" size="large" style={{width: '30%', marginLeft:'30%' }}
                                   prefix={<Icon type="search"/>} placeholder="请输入班次相关信息" onChange={this.onChangeContent}/>
                            <Button type="primary"  size="large" style={{width: '10%', marginLeft: '1%'}} onClick = {this.handleSearch}>搜索</Button>
                            <h6 />
                            <br />
                            <Table
                                components={components}
                                bordered
                                dataSource={this.state.data}
                                columns={columns}
                                rowClassName="editable-row"
                                style={{width:'80%', marginLeft:'10%'}}
                            />
                        </Content>
                    </Layout>
                </Content>
                {context.footer}
            </Layout>
        );
    }

}

export default ModifyShift;

/**
 * Created by 励颖 on 2018/7/2.
 */
