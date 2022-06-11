import React from 'react';

export default function TimeConver(props) {

    const { data: { text = '', record }, options = {} } = props;
    const { path, query = { id: 'id' }, blank = false } = options;

    function setTime(time) {
        var currentTime = Date.parse(new Date());
        var dateTime = time;//后台传递来的时间
        var d_day = Date.parse(new Date(dateTime));
        var year = Math.abs(parseInt((d_day - currentTime) / 1000 / 3600 / 8640));//计算年
        var month = Math.abs(parseInt((d_day - currentTime) / 1000 / 3600 / 720));//计算月
        var day = Math.abs(parseInt((d_day - currentTime) / 1000 / 3600 / 24));//计算日期
        var hour = Math.abs(parseInt((d_day - currentTime) / 1000 / 3600));//计算小时
        var minutes = Math.abs(parseInt((d_day - currentTime) / 1000 / 60));//计算分钟
        var seconds = Math.abs(parseInt((d_day - currentTime) / 1000));//计算秒
        // console.log(day);

        if (year >= 1) {
            return year + "年前";
        } else if (month >= 1) {
            return month + "个月前";
        } else if (day >= 2) {
            return day + "天前";
        } else if (day > 0 && day < 2) {
            return "昨天";
        } else if (hour > 0 && hour < 24) {
            return hour + "小时前";
        } else if (minutes > 0 && minutes < 60) {
            return minutes + "分钟前";
        } else if (seconds > 0 && seconds < 60) {
            return seconds + "秒前";
        }
    }
    
    return (
        <div>{setTime(text)}</div>
    )

}