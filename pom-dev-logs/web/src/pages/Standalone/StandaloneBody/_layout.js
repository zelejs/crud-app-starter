module.exports = {
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
        container: 'PlainList',
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