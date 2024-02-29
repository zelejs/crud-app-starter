import React, { useState, useEffect } from 'react';
import { VStack, Box, Button, useToast, HStack, Text } from '@chakra-ui/react';
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
  presenter: 'presenter',
  cart: 'cart',
  layout: 'layout',
  container: 'container',
}

const apiIdMap = {
  presenter: '197',
  cart: '129',
  layout: '196',
  container: '198',
}

export default function Index(props) {

  const { id, status, cb } = props
  //   const api = '/openapi/lc/module?componentOption=cart'
  const api = '/openapi/lc/module'
  const layoutApi = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules'

  const [currentApi, setCurrentApi] = useState('')
  const [currentLayoutApi, setCurrentLayoutApi] = useState('')
  const [currentPage, setCurrentPage] = useState('presenter')
  const [currentCartId, setCurrentCartId] = useState('')
  const toast = useToast()

  useEffect(() => {
    
    if(currentPage){
      const cApi = `${api}?componentOption=${apiParamsMap[currentPage]}`
      const cLayoutApi = `${layoutApi}/${apiIdMap[currentPage]}`
      setCurrentApi(cApi)
      setCurrentLayoutApi(cLayoutApi)
    }
  }, [currentPage])

  //保存数据
  function createCart(item) {
    let api = '/openapi/lc/module/clone-module/' + item.id
    const queryData = {
    };
    promiseAjax(api, queryData, { method: 'POST' }).then(resp => {
      if (resp && resp.code === 200) {
        setCurrentCartId(resp.data.id)
        getNextComponent(resp.data.id)
      } else {
        console.error("添加cart失败 = ", resp)
        toastTips("添加失败")
      }
    });
  }



  //获取下一步信息
  function getNextComponent(id) {
    setCurrentApi()
    setCurrentLayoutApi()
    let api = '/openapi/lc/module/cart/build-cart/' + id
    const queryData = {
    };
    promiseAjax(api, queryData, { method: 'PATCH' }).then(resp => {
      // setLoading(false)
      if (resp && resp.code === 200) {
        setCurrentPage(resp.data.nextComponent)
      } else {
        console.error("获取下一步信息失败 = ", resp)
        toastTips("获取信息异常")
      }
    });
  }

  //
  function editData(itemData) {
    let api = '/openapi/lc/module/cart/build-cart/' + currentCartId
    const queryData = {
      addModuleId: itemData.id
    };
    promiseAjax(api, queryData, { method: 'PATCH' }).then(resp => {
      if (resp && resp.code === 200) {
        if (resp.data.nextComponent === 'finish') {
          cb('success')
          toastTips('新增成功')
        } else {
          setCurrentPage(resp.data.nextComponent)
        }
      } else {
        console.error("编辑cart失败 = ", resp)
        toastTips(resp.message)
      }
    }).finally(_ => {
    });
  }

  //获取应该跳转到哪一页
  function getNextDataToPage() {
    let api = '/openapi/lc/module/cart/build-cart/' + currentCartId
    const lsSkip = localStorage.getItem('skipComponentOptionList') ? JSON.parse(localStorage.getItem('skipComponentOptionList')) : []
    if (lsSkip && lsSkip.length > 0) {
      lsSkip.push(currentPage)
    }
    const skipComponentOptionList = lsSkip && lsSkip.length > 0 ? lsSkip : [currentPage]

    const queryData = {
      skipComponentOptionList
    };
    promiseAjax(api, queryData, { method: 'PATCH' }).then(resp => {

      if (resp && resp.code === 200) {
        if (resp.data.nextComponent === 'finish') {
          cb('success')
          toastTips('新增成功')
        } else {
          let skipList = []
          if (localStorage.getItem('skipComponentOptionList')) {
            skipList = JSON.parse(localStorage.getItem('skipComponentOptionList'))
            skipList.push(resp.data.nextComponent)
          } else {
            skipList = [currentPage]
          }
          localStorage.setItem('skipComponentOptionList', JSON.stringify(skipList))
          setCurrentApi('')
          setCurrentLayoutApi('')
          setTimeout(() => {
            setCurrentPage(resp.data.nextComponent)
          }, 100)
        }
      } else {
        console.error("跳过失败 = ", resp)
        toastTips("跳过失败")
      }
    });
  }

  //跳过单前页, 进入下一步
  const nextPage = () => {
    getNextDataToPage()
  }

  //保存presenter
  const savePresenters = (presenters) => {
    setCurrentApi('')
    setCurrentLayoutApi('')
    setTimeout(() => {
      setCurrentPage('cart')
    }, 100)
  }

  const onComponentItemClick = (data) => {
    console.log("current page = ", currentPage)
    if (data.isSelected) {
      setCurrentApi('')
      setCurrentLayoutApi('')
      if(currentPage === 'cart'){
        setCurrentPage('layout')
      //   createCart(item)
      }else if(currentPage === 'layout'){
        setCurrentPage('container')
      //   editData(item)
      }else if(currentPage === 'container'){
        cb('success')
        toastTips('新增成功')
      }
    } else if(data && Array.isArray(data) && data.length > 0){
      console.log('多选 = ', data)
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
      <HStack spacing={5} w={'100%'} justifyContent={'space-between'} >
        <div></div>
        <Text fontSize={'16px'} fontWeight={'bold'}>新增组件</Text>
        {
          currentPage != 'presenter' ? (
            <Button colorScheme='teal' size='sm' onClick={() => nextPage()}>
              跳过
            </Button>
          ) : (
            <Button colorScheme='teal' size='sm' onClick={() => savePresenters()}>
              确认
            </Button>
          )
        }
      </HStack>
      <Box style={{ margin: '20px 5px 5px 5px', paddingLeft: '8px' }} >
        {currentApi && currentLayoutApi ? (
          <PreviewAutoLayout layoutApi={currentLayoutApi} api={currentApi} onItemClick={onComponentItemClick} />
        ) : <></>}
      </Box>

    </VStack>
  )

}