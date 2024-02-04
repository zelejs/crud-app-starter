import React from 'react';
import { ChakraProvider, VStack, Box, Button } from '@chakra-ui/react';
import { history } from 'umi';
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';

const routeMap = {
  presenter: '/presenters',
  cart: '/carts',
  layout: '/layouts',
  container: '/containers',
}

const testItems = [
  { id: 1, name: 'test1' },
  { id: 2, name: 'test2' },
  { id: 3, name: 'test3' },
  { id: 4, name: 'test4' },
  { id: 5, name: 'test5' },
  { id: 6, name: 'test6' },
  { id: 7, name: 'test7' },
  { id: 8, name: 'test8' },
]

export default function Index(props) {

  const { id } = props.location && (props.location.query || qs.parse(props.location.search.split('?')[1]))
  const api = '/openapi/lc/module?componentOption=layout'
  const layoutApi = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/153'

  //保存数据
  function saveData(itemData) {
    let api = '/openapi/lc/module/build-auto-layout/' + id
    const queryData = {
      addModuleId: itemData.id
    };
    promiseAjax(api, queryData, { method: 'PATCH' }).then(resp => {
      setLoading(false)
      if (resp && resp.code === 200) {
        toPage(resp.data.nextComponent)
      } else {
        console.error("添加layout失败 = ", resp)
        toastTips("添加失败")
      }
    });
  }

  //返回详情页
  function goViewPage() {
    history.push({
      pathname: '/view',
      query: {
        id
      }
    })
  }

  //获取应该跳转到哪一页
  function getNextDataToPage() {
    let api = '/openapi/lc/module/build-auto-layout/' + id

    const skipComponentOptionList = JSON.parse(localStorage.getItem('skipComponentOptionList'))
    const queryData = {
      skipComponentOptionList
    };
    promiseAjax(api, queryData, { method: 'PATCH' }).then(resp => {
      setLoading(false)
      if (resp && resp.code === 200) {
        const skipList = skipComponentOptionList
        skipList.push(resp.data.nextComponent)
        localStorage.setItem('skipComponentOptionList', JSON.stringify(skipList))
        toPage(resp.data.nextComponent)
      } else {
        console.error("获取数据失败 = ", resp)
        toastTips("获取数据失败")
      }
    });
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
    if (itemData.isSelected) {
      saveData(itemData)
    }
  }

  return (
    <ChakraProvider>
      <VStack align='stretch' spacing='-2'>
        <Box style={{ margin: '5px 10px 15px 5px', paddingLeft: '8px' }}>
            <Button colorScheme='teal' size='sm' marginRight={'8px'} onClick={() => goViewPage()}>
              返回
            </Button>
            <Button colorScheme='teal' size='sm' onClick={() => nextPage()}>
              跳过
            </Button>
        </Box>

        <Box style={{ margin: '5px 5px 5px 5px', paddingLeft: '8px' }} >
          <PreviewAutoLayout layoutApi={layoutApi} api={api} onPreviewItemClick={onComponentItemClick} />
        </Box>
      </VStack>
    </ChakraProvider>
  )

}