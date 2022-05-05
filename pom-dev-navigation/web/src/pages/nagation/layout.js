module.exports = {
  xname: 'Gridbox',
  props: {
    direction: 'column',
    justify: 'center row'
  },
  presenter: {
    xname: 'Flexbox',
    props: {
      direction: 'column',
      justify: 'center row'
    },
    children: [
      {
        presenter: 'Avatar',
        gateway: {
          xname: 'Binding',
          props: {
            binding: {
              name: 'url'
            }
          }
        }
      },
      {
        presenter: "Title",
        gateway: {
          xname: "Binding",
          props: {
            binding: {
              name: "titleText"
            }
          }
        }
      }]
  },
  cart: {
    xname: 'Cart',
    props: {
      padding: '5px 10px',
      margin: '5px',
      linewidth: 0
    }
  },
  container: 'SimCRUDList',
  navigation: {
    model: {
      api: {
        createAPI: '/api/c/navigation/navigations',
        getAPI: '/api/c/navigation/navigations/(id)',
        updateAPI: '/api/c/navigation/navigations/(id)',
        deleteAPI: '/api/c/navigation/navigations/(id)'
      },
      fields: [{
        label: '标题',
        field: 'name',
        type: 'input',
        required: {
          placeholder: '请输入标题'
        }
      },
      {
        label: '图片',
        field: 'url',
        type: 'input',
        required: {
          placeholder: '图片链接'
        }
      },
      {
        label: '描述',
        field: 'desc',
        type: 'input',
        // height: '25px',
        required: {
          placeholder: '描述属性'
        }
      }
        ,
      {
        label: '链接',
        field: 'path',
        type: 'input',
        required: {
          placeholder: '请输入本地链接/第三方以https开头'
        }
      }
      ]
    }
  }
};
