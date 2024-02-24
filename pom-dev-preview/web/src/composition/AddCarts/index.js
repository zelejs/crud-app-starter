import React, { useState, useEffect } from 'react';
import { VStack, Box, Button, useToast  } from '@chakra-ui/react';
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
    indicator: '161',
    selector: '163',
}

export default function Index (props) {

  const { id, status, cb } = props
//   const api = '/openapi/lc/module?componentOption=cart'
  const api = '/openapi/lc/module'
  const layoutApi = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules'
  
  const [ currentApi, setCurrentApi ] = useState('')
  const [ currentLayoutApi, setCurrentLayoutApi ] = useState('')
  const [ currentPage, setCurrentPage ] = useState('cart')
  const toast = useToast()

  useEffect(() => {
      const cApi = `${api}?componentOption=${apiParamsMap[currentPage]}`
      const cLayoutApi = `${layoutApi}/${apiIdMap[currentPage]}`
      setCurrentApi(cApi)
      setCurrentLayoutApi(cLayoutApi)
  },[currentPage])

  console.log('currentApi', currentPage, currentApi, currentLayoutApi)

  //保存数据
  function saveData(itemData) {
    let api = '/openapi/lc/module/build-auto-layout/'+id
    const queryData = {
        addModuleId:itemData.id,
    };
    promiseAjax(api, queryData, {method: 'PATCH'}).then(resp => {
        // setLoading(false)
        if (resp && resp.code === 200) {
            toPage(resp.data.nextComponent)
        } else {
            console.error("添加cart失败 = ", resp)
            toastTips("添加失败")
        }
    });
  }

  //更换
  function editData(itemData) {
    let api = '/openapi/lc/module/AutoLayout/replaceModule/' + id
    const queryData = {
      replaceModuleId:itemData.id
    };
    promiseAjax(api, queryData, { method: 'PUT' }).then(resp => {
      if (resp && resp.code === 200) {
        goViewPage()
      } else {
        console.error("更换cart失败 = ", resp)
        toastTips(resp.message)
      }
    }).finally(_ => {
      // setLoading(false)
    });
  }

  //获取应该跳转到哪一页
  function getNextDataToPage(itemData) {
    let api = '/openapi/lc/module/build-auto-layout/'+id
    const queryData = {
        skipComponentOptionList: ["cart"]
    };
    promiseAjax(api, queryData, {method: 'PATCH'}).then(resp => {
        // setLoading(false)
        if (resp && resp.code === 200) {
            const skipList = ["cart"]
            skipList.push(resp.data.nextComponent)
            localStorage.setItem('skipComponentOptionList', JSON.stringify(skipList))
            toPage(resp.data.nextComponent)
        } else {
            console.error("跳过失败 = ", resp)
            toastTips("跳过失败")
        }
    });
  }

  //返回详情页
  function goViewPage(){
    history.push({
        pathname:'/view',
        query:{
            id
        }
    })
  }

  const toPage = (nextComponent) => {
    const path = routeMap[nextComponent]
    history.push({
        pathname: path,
        query: {
            id
        }
    })
  }

  //跳过单前页, 进入下一步
  const nextPage = () => {
    getNextDataToPage()
  }

  const onComponentItemClick = (item) => {
    setCurrentApi('')
    setCurrentLayoutApi('')
    if (item.isSelected) {
        if(currentPage === 'cart'){
            setCurrentPage('indicator')
        }else if(currentPage === 'indicator'){
            setCurrentPage('selector')
        }else{
            // setCurrentPage('cart')
            cb('success')
            toastTips('新增成功')

        }
    //   if(status === 'edit'){
    //     editData(item)
    //   }else{
    //     saveData(item)
    //   }
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
        {/* <Box style={{ margin: '5px 10px 15px 5px', paddingLeft: '8px' }}>
                <Button colorScheme='teal' size='sm' marginRight={'8px'} onClick={() => goViewPage()}>
                    返回
                </Button>
                { status != 'edit' && (
                <Button colorScheme='teal' size='sm' onClick={() => nextPage()}>
                    跳过
                </Button>
                ) }
                
        </Box> */}
        <Box style={{ margin: '5px 5px 5px 5px', paddingLeft: '8px' }} >
            { currentApi && currentLayoutApi ? (
                <PreviewAutoLayout  layoutApi={currentLayoutApi} api={currentApi} onItemClick={onComponentItemClick} />
            ):<></>}
        </Box>

    </VStack>
  )

}