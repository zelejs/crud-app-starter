import React from 'react';
import { Box } from "@chakra-ui/react";
import  AutoLayout  from '@/components/AutoLayout';
import useTokenRequest from '@/components/hooks/useTokenRequest';

export default function Index (props) {

  // 参数
  const {api,layoutApi, layoutName, layoutId, ...rest} = props;

  // 判断 layoutApi 是否为空，如果为空，则用 layoutName 拼接api路径
  let localLayoutApi = ''
  if(layoutApi || layoutName){
    localLayoutApi = layoutApi || '/openapi/lc/module/getAutoLayout/' + layoutName
  }else if(layoutId){
    localLayoutApi = `/form?id=${layoutId}`
  }

  // 从api获取显示数据
  const [ data ] = useTokenRequest({ api });
  const records = data && data.records;
  const dataX = []
  dataX.push({ items: records })

  // 从layoutApi获取layoutJson
  const respLayoutData = useTokenRequest({ api: localLayoutApi });
  const layoutJson = respLayoutData && respLayoutData[0]
  console.log('layoutJson===',respLayoutData)

  /**
   * 页面配置
   */
  const config = {
    items: dataX.length > 0 ? dataX : [],
    layout: layoutJson,
    ...rest
  };

  // 控制台输出信息
  const onPreviewItemClick = (item) => {
    //TODO
    console.log(item, ' === item')
  }

  return (
    <Box spacing='3px'>
        <AutoLayout {...config} onItemClick={onPreviewItemClick} />
    </Box>
  )
}