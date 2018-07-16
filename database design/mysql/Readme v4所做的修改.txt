
数据库v4相对v3所做的修改

1. 新增JaccountUsers用户表

2. Appointment 表新增 username varchar（50）属性

3. 取消Appointment表 userid的外键依赖（以后写的时候注意一点）

4. 删除Bus表中的shift_id

5. Shift表 新增 bus_id int属性，目前为shift一对多bus

6. Shift表 新增 arrive_time Time 属性

7. 调整drop table的顺序，将drop Shift 移到drop Bus之前