import { setEndpoint, setToken } from 'zero-element-boot/lib/components/config/common';

// import { set as NamedCartSet } from 'zero-element-boot/lib/config/NamedCartConfig';
import { set as NamedIndicatorSet } from 'zero-element-boot/lib/components/config/NamedIndicatorConfig';
import { set as NamedPresenterSet } from 'zero-element-boot/lib/components/config/NamedPresenterConfig';

// import JarItem from '@/composition/Standalone/JarItem';


// //cart
// import Cart from 'zero-element-boot/lib/components/cart/Cart';

//indicator
//layout


//presenter


// NamedCartSet({
//   Cart
// })
// NamedLayoutSet({
//   Flexbox,
//   Wrap
// })
NamedIndicatorSet({

})

NamedPresenterSet({
    // JarItem 
})

//开发模式设置endpoint, token
if(process.env.NODE_ENV == 'development'){
    setEndpoint('http://static.smallsaas.cn')
    
    // setToken('eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJvcmdJZCI6MzAsInVzZXJJZCI6MzE4LCJhY2NvdW50IjoiYjQxYTg2OTRhNjM4NDIyNWJmNWMxOTQyZjdmZjIyNTYiLCJkb21haW5Vc2VySWQiOiIiLCJ0eXBlIjoxNDYxLCJpYXQiOjE2NjkxMTMzMTQsImp0aSI6IjMxOCIsInN1YiI6ImI0MWE4Njk0YTYzODQyMjViZjVjMTk0MmY3ZmYyMjU2IiwiZXhwIjoxNjY5MzcyNTE0fQ.HnIuikkC0ugUeZAoqhtVgl4wYiJUgkuL9v8kd46YNIBpdCj-DujDVfHIUgfCqjp0mP23o-3hP697swHrP2qPiQ')

}

