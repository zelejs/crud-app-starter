import Index from 'zero-element-antd/lib/valueType/index';
// import Video from 'zero-element-antd/lib/valueType/Video';
import Plain from 'zero-element-antd/lib/valueType/plain';
import Join from 'zero-element-antd/lib/valueType/join';
// import Map from 'zero-element-antd/lib/valueType/map';
import Tag from 'zero-element-antd/lib/valueType/tag';
import Image from 'zero-element-antd/lib/valueType/image';
// import Thumb from 'zero-element-antd/lib/valueType/thumb';
// import Dot from 'zero-element-antd/lib/valueType/dot';
// import Currency from 'zero-element-antd/lib/valueType/currency';
// import Percentage from 'zero-element-antd/lib/valueType/percentage';
// import Url from 'zero-element-antd/lib/valueType/url';
// import Download from 'zero-element-antd/lib/valueType/download';
// import Ellipsis from 'zero-element-antd/lib/valueType/ellipsis';
// import Complex from 'zero-element-antd/lib/valueType/complex';
// import CountDown from 'zero-element-antd/lib/valueType/countDown';
// import InputNumber from 'zero-element-antd/lib/valueType/inputNumber';
// import InputText from 'zero-element-antd/lib/valueType/inputText';
// import InputSelect from 'zero-element-antd/lib/valueType/inputSelect';
// import InputSelectFetch from 'zero-element-antd/lib/valueType/inputSelectFetch';
import TimeConvert from 'zero-element-antd/lib/valueType/timeConvert';

export default {
    records: [
        { 
            id: 1, 
            label: 'index', 
            compx: Index, 
            type: '-',
            value: { 
                model: { 
                    listData:{ 
                        current: 1, 
                        pageSize: 10
                    }
                }, 
                data: { 
                    index: 1
                }
            },
            jsonFormat: '{ \n "label": "index",  \n "field": "index",  \n "valueType": "index" \n }',
            describe: '索引'
        },
        { 
            id: 2, 
            label: 'plain', 
            compx: Plain, 
            type: 'string / number',
            value: { 
                data: { 
                    text: 'plain' 
                } 
            },
            jsonFormat: '{ \n "label": "plain",  \n "field": "fieldName",  \n "valueType": "plain" \n }',
            describe: '文本'
        },
        { 
            id: 3, 
            label: 'video', 
            compx: null, 
            value: { 
                data: { 
                    text: '' 
                } 
            },
            describe: '视频链接'
        },
        
        { 
            id: 4, 
            label: 'join', 
            compx: Join, 
            type: 'string / array',
            value: { 
                data: { 
                    text: ['join-1', 'join-2'] 
                } 
            },
            jsonFormat: '{ \n "label": "join",  \n "field": "fieldName",  \n "valueType": "join" \n }',
            describe: '文本数组'
        },

        // { 
        //     id: 5, 
        //     label: 'map', 
        //     compx: Map, 
        //     type: 'object',
        //     value: { 
        //         options: {
        //             map:{
        //                 "0":"否",
        //                 "1":"是" 
        //             },
        //         },
        //         data: {
        //             text: '0'
        //         }
        //     },
        //     jsonFormat: '{ \n "label": "map",  \n "field": "fieldName",  \n "valueType": "map", \n "options": { "0":"否", "1":"是" }\n }',
        //     describe: '标签'
        // },

        { 
            id: 6, 
            label: 'tag', 
            compx: Tag, 
            type: 'string',
            value: { 
                options: {
                    map:{
                        "Open": "打开",
                        "Closed": "已关闭"
                    },
                    color: {
                        "Open": "#0000FF",
                        "Closed": "#C1CDC1"
                    }
                },
                data: {
                    text: 'Open'
                }
            },
            jsonFormat: '{ \n "label": "tag",  \n "field": "fieldName",  \n "valueType": "tag", \n "options": { \n "map":{ \n "Open":"打开", \n "Closed":"已关闭" \n }, \n "color":{ \n "Open":"#0000FF", \n "Closed":"#C1CDC1" \n }',
            describe: '标签(背景)'
        },

        { 
            id: 7, 
            label: 'image', 
            compx: Image, 
            type: 'url',
            value: { 
                data: {
                    text: 'https://gw.alipayobjects.com/zos/rmsportal/KDpgvguMpGfqaHPjicRK.svg'
                }
            },
            jsonFormat: '',
            describe: '图片'
        },

        // { 
        //     id: 8, 
        //     label: 'url', 
        //     compx: Url, 
        //     type: 'string',
        //     value: { 
        //         data: {
        //             text: '/userAudit/applyManage'
        //         }
        //     },
        //     jsonFormat: '',
        //     describe: '跳转路由'
        // },

        // { 
        //     id: 9, 
        //     label: 'download', 
        //     compx: Download, 
        //     type: 'string',
        //     value: { 
        //         data: {
        //             text: 'www.baidu.com'
        //         },
        //         options:{
        //             fieldName: ''
        //         }
        //     },
        //     jsonFormat: '',
        //     describe: '下载按钮'
        // },

        { 
            id: 10, 
            label: 'time-convert', 
            compx: TimeConvert, 
            type: 'string',
            value: { 
                data: {
                    text: '2022-6-15 10:54:50'
                },
            },
            jsonFormat: '',
            describe: '显示已过去时间'
        },


    ]
}