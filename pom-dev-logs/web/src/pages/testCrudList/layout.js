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
      justify: 'center row',
    },
    children: [

      {
        presenter: 'Avatar',
        gateway: {
          xname: 'Binding',
          props: {
            binding: {
              avatar: 'url'
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


      }
    ]
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
        createAPI: '/api/v/navigation/navigations',
        getAPI: '/api/v/navigation/navigations/(id)',
        updateAPI: '/api/v/navigation/navigations/(id)',
        deleteAPI: ''
      },
      fileds: [{
        label: '名字',
        field: 'name',
        type: 'input',
        required: {
          placeholder: '请输入名字',
        }

      }]
    }
  }

}
