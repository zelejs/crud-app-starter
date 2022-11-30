import React, { useState, useEffect } from 'react';
// import useTokenRequest from '@/components/hooks/useTokenRequest';
import Tree from '@/composition/JsonTree'
const promiseAjax = require('zero-element-boot/lib/components/utils/request');
import Loading from 'zero-element-boot/lib/components/loading';

export default function index(props) {

    const { api, compParams } = props
    const params = (props.location && ( props.location && (props.location.query ||  qs.parse(props.location.search.split('?')[1])) )) || compParams

    const [ jsonData, setJsonData ] = useState('')
    const [ isLoading, setIsLoading ] = useState(false);
    
    // const api = params.api || `/openapi/lc/apis/${params.apiName}`
    // const api = `/api/ContentTest`
    // const [data] = useTokenRequest({ api })

    useEffect(_ => {
        setJsonData('')
        if((params && params.api) || api){
            if(params && params.api){
                setIsLoading(true)
            }
            const apiUrl = params && params.api ? params.api : api
            getJsonDataByApi(apiUrl)
        }else if(params && params.apiName){
            setIsLoading(true)
            getApiUrlByApiName()
        }
    },[params, api]);

    //通过 apiName 获取 api路径
    function getApiUrlByApiName(){
        promiseAjax(`/openapi/lc/apis/${params.apiName}`)
            .then(res => {
                if (res && res.code === 200) {
                    let data = res.data
                    let apiValue = data.api
                    if(apiValue.indexOf('{') != -1 && params.layoutName){
                        apiValue = convertApiUrl(apiValue, params.layoutName)
                        getJsonDataByLayoutName(apiValue)
                        setIsLoading(false)
                    }else{
                        getJsonDataByApi(apiValue)
                    }
                }else{
                    
                }
            })
    }

    //转换API参数
    function convertApiUrl (api, layoutName) {
        let regex = /\{(.*?)\}/g; //匹配<*> 大括号里面任意内容的正则
        let arr = api.match(regex); //字符串匹配出来的数组
        let formatString = api
        arr.map(item => {
            formatString = formatString.replace(`${item}`, layoutName)
        })
        return formatString
    }

    //通过 layoutName 获取 api路径
    function getJsonDataByLayoutName(api){
        promiseAjax(api, params)
            .then(res => {
                if (res && res.code === 200) {
                    let data = res.data
                    console.log('getJsonDataByLayoutName data = ', data)
                    setJsonData(data)
                }
            })
    }

    //
    function getJsonDataByApi(api){
        promiseAjax(api, params)
            .then(res => {
                if (res && res.code === 200) {
                    let data = res.data
                    data = Array.isArray(data)? data[0] : typeof data === 'object' ? data : {}
                    setJsonData(data)
                }else{
                    
                }
                setIsLoading(false)
            })
    }

    return (
        <>
            {/* <TreeItem keyName="xname" {...data}/> */}
            {isLoading ? (
                <Loading styles={{marginTop: '10px', marginLeft: '5px'}}/>
            ):(
                jsonData && JSON.stringify(jsonData) !== '{}' && <Tree {...jsonData} />
            )}
            
        </>
    )




}