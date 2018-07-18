
数据库v4相对v3所做的修改

1. 新增JaccountUsers用户表

2. Appointment 表新增 username varchar（50）属性

3. 取消Appointment表 userid的外键依赖（以后写的时候注意一点）

4. 删除Bus表中的shift_id

5. Shift表 新增 bus_id int属性，目前为shift一对多bus

6. Shift表 新增 arrive_time Time 属性

7. 调整drop table的顺序，将drop Shift 移到drop Bus之前

------------------------------------------------------------

v4.1所做的修改

1. 纠正appoint表中appointment_id的拼写错误

2. appoint表添加user_code（yzh用）和submit_time（wxw用）

3. 修改bus的车牌号为三辆校园巴士的真实车牌，校区巴士未知。