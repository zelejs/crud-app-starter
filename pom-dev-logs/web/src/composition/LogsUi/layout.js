module.exports = {
  xname: 'Flexbox',
  props: {
    align: 'start',
    direction: 'row'
  },
  // gateway: {
  //   xname: 'Binding',
  //   props: {
  //     binding: {
  //       url: 'url',
  //       title: 'title',
  //       describe: 'describe',
  //       adType: 'adType',
  //       createTime: 'createTime'
  //     }
  //   }
  // },
  cart: {
    xname: 'Cart',
    props: {
      // padding: '16px',
      margin: '0',
      corner: 0,
      linewidth: '0',
      padding: '10px 25px',
      isOnHover: false
    }
  },
  container: 'PlainList',
  presenter: {
    xname: 'Flexbox',
    props: {
      direction: 'column',
      justify: 'center row'
    },
    children: [
      {
        presenter: {
          xname: 'Flexbox',
          props: {
            align: 'start',
            direction: 'column',
            flexWidth: 'auto-full'
          },
          presenter: 'JarItem',
          cart: {
            xname: 'Cart',
            props: {
              isOnHover: true,
              margin: '0px 0px 2px 0px',
              linewidth: 1,
              padding: '0px'
            }
          },
          container: 'ItemClickList',
        },
        gateway: {
          xname: 'Binding',
          props: {
            binding: {
              items: 'items'
            }
          }
        }
      },
    ]
  }
}