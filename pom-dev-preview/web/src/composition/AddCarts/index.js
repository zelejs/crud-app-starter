import React, { useState, useEffect } from 'react';
import { VStack, Box, Button, useToast, HStack, Text  } from '@chakra-ui/react';
import { history } from 'umi';
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');

const routeMap = {
  presenter: '/presenters',
  cart: '/carts',
  layout: '/layouts',
  container: '/containers',
  finish: '/view'
}

const apiParamsMap = {
    cart: 'cart',
    indicator: 'indicator',
    selector: 'selector',
}

const apiIdMap = {
    cart: '129',
    indicator: '179',
    selector: '180',
}

export default function Index (props) {

  const { id, status, cb } = props
//   const api = '/openapi/lc/module?componentOption=cart'
  const api = '/openapi/lc/module'
  const layoutApi = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules'
  
  const [ currentApi, setCurrentApi ] = useState('')
  const [ currentLayoutApi, setCurrentLayoutApi ] = useState('')
  const [ currentPage, setCurrentPage ] = useState('')
  const [ currentComponentId, setCurrentComponentId ] = useState('') // mainModuleId
  const [ currentSkipComponentOptionList, setCurrentSkipComponentOptionList] = useState([])
  const toast = useToast()

  useEffect(() => {
    getPostSkipComponent()
  },[])

  useEffect(() => {
    if(currentPage){
      let cApi = `${api}?componentOption=${apiParamsMap[currentPage]}`
      if(currentPage === 'cart'){
        cApi += '&moduleType=native'
      }
      const cLayoutApi = `${layoutApi}/${apiIdMap[currentPage]}`
      setCurrentApi(cApi)
      setCurrentLayoutApi(cLayoutApi)
    }
  },[currentPage])

  //处理数据
  function getPostSkipComponent(item, skipData) {
    setCurrentApi()
    setCurrentLayoutApi()
    let api = '/openapi/lc/module/cart/build-cart'
    const queryData = {
    };
    if(currentComponentId){
      queryData.mainModuleId = currentComponentId
    }
    if(item &&  item.id){
      queryData.addModuleId = item.id
    }
    if(currentSkipComponentOptionList && currentSkipComponentOptionList.length > 0){
      const newSkipList = currentSkipComponentOptionList
      if(skipData){
        newSkipList.push(skipData)
      }
      queryData.skipComponentOptionList = newSkipList
    } else if(skipData) {
      queryData.skipComponentOptionList = [skipData]
    }

    promiseAjax(api, queryData, {method: 'POST'}).then(resp => {
        if (resp && resp.code === 200) {
          if(!currentComponentId && resp.data.mainModuleId){
            setCurrentComponentId(resp.data.mainModuleId)
          }
          if(resp.data.nextComponent === 'finish'){
            cb('success')
            toastTips('新增成功')
          }else{
            setCurrentPage(resp.data.nextComponent)
          }
          if(resp.data.skipComponentOptionList && resp.data.skipComponentOptionList.length > 0){
            setCurrentSkipComponentOptionList(resp.data.skipComponentOptionList)
          }
        } else if ( resp.code === 4000 ) {
          cb('error')
          toastTips(resp.message)
        }  else {
            console.error("getPostComponent = ", resp)
            toastTips(resp.message)
        }
    });
  }

  const onComponentItemClick = (item) => {
    setCurrentApi('')
    setCurrentLayoutApi('')
    if (item.isSelected) {
      getPostSkipComponent(item)
    }
  }

  // tips
  function toastTips(text, status = 'success') {
    toast({
        title: text,
        description: "",
        status: status,
        duration: 3000,
        isClosable: true,
        position: 'top'
    })
  }

  return (

    <VStack align='stretch' spacing='-2'>
        <HStack spacing={5} w={'100%'} justifyContent={'space-between'}>
          <div></div>
          <Text fontSize={'16px'} fontWeight={'bold'}>新增组件</Text>
          <Button colorScheme='teal' size='sm' onClick={() => getPostSkipComponent("", currentPage)}>
            跳过
          </Button>
        </HStack>
        <Box style={{ margin: '20px 5px 5px 5px', paddingLeft: '8px' }} >
            { currentApi && currentLayoutApi ? (
                <PreviewAutoLayout  layoutApi={currentLayoutApi} api={currentApi} onItemClick={onComponentItemClick} />
            ):<></>}
        </Box>

    </VStack>
  )

}