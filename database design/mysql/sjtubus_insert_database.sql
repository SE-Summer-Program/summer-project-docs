
insert into Administrator values('000001', 'admin', 'admin');
insert into Administrator values('000002', 'public', 'public');

insert into Users values('100001', 'yzh', 'password', '100', '0');
insert into Users values('100002', 'wxw', 'password', '100', '0');
insert into Users values('100003', 'ly', 'password', '100', '0');
insert into Users values('100004', 'wyl', 'password', '100', '0');
insert into Users values('100005', 'sbj', 'password', '100', '1');
insert into Users values('100006', 'rr', 'password', '100', '1');
insert into Users values('100007', 'chp', 'password', '100', '1');

insert into Driver values('200001', 'driver1', 'driver1', '13262600000');
insert into Driver values('200002', 'driver2', 'driver2', '18767600000');

insert into Shift values('LLAW0730', 'LoopLineAntiClockwise', '校园巴士逆时针', '工作日', '7:30:00', '50', '');
insert into Shift values('LLAW0745', 'LoopLineAntiClockwise', '校园巴士逆时针', '工作日', '7:45:00', '50', '');
insert into Shift values('LLAW0800', 'LoopLineAntiClockwise', '校园巴士逆时针', '工作日', '8:00:00', '50', '');
insert into Shift values('LLAW0815', 'LoopLineAntiClockwise', '校园巴士逆时针', '工作日', '8:15:00', '50', '');
insert into Shift values('LLAW0825', 'LoopLineAntiClockwise', '校园巴士逆时针', '工作日', '8:25:00', '50', '');
insert into Shift values('LLAW0840', 'LoopLineAntiClockwise', '校园巴士逆时针', '工作日', '8:40:00', '50', '');
insert into Shift values('LLCW0830', 'LoopLineClockwise', '校园巴士顺时针', '工作日', '8:30:00', '50', '');
insert into Shift values('LLCW0850', 'LoopLineClockwise', '校园巴士顺时针', '工作日', '8:50:00', '50', '');
insert into Shift values('LLCW0910', 'LoopLineClockwise', '校园巴士顺时针', '工作日', '9:10:00', '50', '');
insert into Shift values('LLAH0815', 'LoopLineAntiClockwise', '校园巴士逆时针', '寒暑假', '8:15:00', '50', '');
insert into Shift values('LLAH0930', 'LoopLineAntiClockwise', '校园巴士逆时针', '寒暑假', '9:30:00', '50', '');
insert into Shift values('LLAH1030', 'LoopLineAntiClockwise', '校园巴士逆时针', '寒暑假', '10:30:00', '50', '');
insert into Shift values('LLCH0845', 'LoopLineClockwise', '校园巴士顺时针', '寒暑假', '8:45:00', '50', '');
insert into Shift values('LLCH1000', 'LoopLineClockwise', '校园巴士顺时针', '寒暑假', '10:00:00', '50', '');
insert into Shift values('LLCH1100', 'LoopLineClockwise', '校园巴士顺时针', '寒暑假', '11:00:00', '50', '');
insert into Shift values('MXWD0640A', 'MinHangToXuHui', '闵行到徐汇', '工作日周一至周五', '6:40:00', '50', '');
insert into Shift values('MXWD0640B', 'MinHangToXuHui', '闵行到徐汇', '工作日周一至周五', '6:40:00', '50', '');
insert into Shift values('MXWE0730', 'MinHangToXuHui', '闵行到徐汇', '工作日周六周日', '7:30:00', '50', '');
insert into Shift values('MXWE1630', 'MinHangToXuHui', '闵行到徐汇', '工作日周六周日', '12:15:00', '50', '');
insert into Shift values('MXHD0730', 'MinHangToXuHui', '闵行到徐汇', '寒暑假周一至周五', '7:30:00', '50', '');
insert into Shift values('MXHD1215', 'MinHangToXuHui', '闵行到徐汇', '寒暑假周一至周五', '12:15:00', '50', '');
insert into Shift values('MXHE0730', 'MinHangToXuHui', '闵行到徐汇', '寒暑假周六周日', '7:30:00', '50', '');
insert into Shift values('MXHE1630', 'MinHangToXuHui', '闵行到徐汇', '寒暑假周六周日', '16:30:00', '50', '');
insert into Shift values('XMWD0645A', 'XuHuiToMinHang', '徐汇到闵行', '工作日周一至周五', '6:45:00', '50', '天钥路始发，途径上中、罗阳至闵行 ');
insert into Shift values('XMWD0645B', 'XuHuiToMinHang', '徐汇到闵行', '工作日周一至周五', '6:45:00', '50', '田林始发，途径古美至闵行');
insert into Shift values('XMWD0700A', 'XuHuiToMinHang', '徐汇到闵行', '工作日周一至周五', '7:00:00', '50', '徐汇校区始发');
insert into Shift values('XMWD0700B', 'XuHuiToMinHang', '徐汇到闵行', '工作日周一至周五', '7:00:00', '50', '交大新村始发');
insert into Shift values('XMWE0830', 'XuHuiToMinHang', '徐汇到闵行', '工作日周六周日', '8:30:00', '50', '');
insert into Shift values('XMWE1730', 'XuHuiToMinHang', '徐汇到闵行', '工作日周六周日', '17:30:00', '50', '');
insert into Shift values('XMHD0730A', 'XuHuiToMinHang', '徐汇到闵行', '寒暑假周一至周五', '7:30:00', '50', '直达闵行，途径天钥、上中、罗阳');
insert into Shift values('XMHD0730B', 'XuHuiToMinHang', '徐汇到闵行', '寒暑假周一至周五', '7:30:00', '50', '交大新村始发');
insert into Shift values('XMHD0730C', 'XuHuiToMinHang', '徐汇到闵行', '寒暑假周一至周五', '7:30:00', '50', '田林始发');
insert into Shift values('XMHD0730D', 'XuHuiToMinHang', '徐汇到闵行', '寒暑假周一至周五', '7:30:00', '50', '古美始发');
insert into Shift values('XMHE0830', 'XuHuiToMinHang', '徐汇到闵行', '寒暑假周六周日', '8:30:00', '50', '');
insert into Shift values('XMHE1730', 'XuHuiToMinHang', '徐汇到闵行', '寒暑假周六周日', '17:30:00', '50', '');
insert into Shift values('QMWD0700', 'QiBaoToMinHang', '七宝到闵行', '工作日', '7:00:00', '50', '');
insert into Shift values('QMWD1110', 'QiBaoToMinHang', '七宝到闵行', '工作日', '11:10:00', '50', '');
insert into Shift values('QMWD1610', 'QiBaoToMinHang', '七宝到闵行', '工作日', '16:10:00', '50', '');
insert into Shift values('QMWD1950', 'QiBaoToMinHang', '七宝到闵行', '工作日', '19:50:00', '50', '');
insert into Shift values('QMWE0800', 'QiBaoToMinHang', '七宝到闵行', '寒暑假', '8:00:00', '50', '');
insert into Shift values('QMWE1610', 'QiBaoToMinHang', '七宝到闵行', '寒暑假', '16:10:00', '50', '');
insert into Shift values('MQWD0800', 'MinHangToQiBao', '闵行到七宝', '工作日', '8:00:00', '50', '');
insert into Shift values('MQWD1220', 'MinHangToQiBao', '闵行到七宝', '工作日', '12:20:00', '50', '');
insert into Shift values('MQWD1700', 'MinHangToQiBao', '闵行到七宝', '工作日', '17:00:00', '50', '');
insert into Shift values('MQWD2030', 'MinHangToQiBao', '闵行到七宝', '工作日', '20:30:00', '50', '');
insert into Shift values('MQWE0850', 'MinHangToQiBao', '闵行到七宝', '寒暑假', '8:50:00', '50', '');
insert into Shift values('MQWE1700', 'MinHangToQiBao', '闵行到七宝', '寒暑假', '17:00:00', '50', '');

insert into Bus values('400001', '200001', 'LLAW0730', '50', '沪AB2333');
insert into Bus values('400002', '200002', 'LLAW0745', '50', '沪AB2334');
insert into Bus values('400003', '200001', 'LLAW0800', '50', '沪AB2335');
insert into Bus values('400004', '200002', 'LLAW0815', '50', '沪AB2336');

insert into Appointment values('500001', '100001', 'LLAW0825', '2018-07-09', '校园巴士逆时针', '1');
insert into Appointment values('500002', '100002', 'LLAW0840', '2018-07-10', '校园巴士逆时针', '1');
insert into Appointment values('500003', '100003', 'MQWD0800', '2018-07-11', '校园巴士逆时针', '1');
insert into Appointment values('500004', '100004', 'MQWD1220', '2018-07-12', '校园巴士逆时针', '1');
insert into Appointment values('500005', '100005', 'MQWD1700', '2018-07-13', '校园巴士逆时针', '1');
insert into Appointment values('500006', '100006', 'MQWD2030', '2018-07-14', '校园巴士逆时针', '1');
insert into Appointment values('500007', '100007', 'MQWE0850', '2018-07-15', '校园巴士逆时针', '0');

insert into RideBusInfo values('600001', '2018-07-09', 'LLAW0730', '400001', '0', '1', '50', '45', '5', '40', '0');






