/**
 * Created by sail on 2017/6/1.
 */
import WeCropper from '../we-cropper/we-cropper.js'

const device = wx.getSystemInfoSync()
const width = device.windowWidth
const height = device.windowHeight - 50

Page({
  data: {
    cropperOpt: {
      id: 'cropper',
      width,
      height,
      scale: 2.5,
      zoom: 8,
      cut: {
        x: (width - 300) / 2,
        y: (height - 300) / 2,
        width: 300,
        height: 300
      }
    }
  },
  touchStart (e) {
    this.wecropper.touchStart(e)
  },
  touchMove (e) {
    this.wecropper.touchMove(e)
  },
  touchEnd (e) {
    this.wecropper.touchEnd(e)
  },
  getCropperImage () {
    this.wecropper.getCropperImage((src) => {
      if (src) {
        console.log(src)
        // wx.previewImage({
        //   current: '', // 当前显示图片的http链接
        //   urls: [src] // 需要预览的图片http链接列表
        // })
        // const tempFilePath = src.tempFilePaths[0]
        const tempFilePath = src;
        //  获取裁剪图片资源后，给data添加src属性及其值
        wx.uploadFile({
          // url: 'https://ghlp.lidechenyan.club/upload',
          url: 'http://192.168.1.101:8080/change/upload',
          filePath: tempFilePath,
          name: 'file',
          success: function (res) {
            var data = res.data;
            console.log('data:', data)
            if (data=='true') {
              wx.showToast({
                title: '签到成功',
                icon: 'loading',
                duration: 2000
              })
            } else {
              wx.showToast({
                title: '签到失败',
                icon: 'loading',
                duration: 2000
              })
            }
            
          }
        })
      } else {
        console.log('获取图片地址失败，请稍后重试')
      }
    })
  },
  uploadTap () {
    const self = this

    wx.chooseImage({
      count: 1, // 默认9
      sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
      sourceType: ['camera'], // 可以指定来源是相册还是相机，默认二者都有
      success (res) {
        const tempFilePath = res.tempFilePaths[0]
        // //  获取裁剪图片资源后，给data添加src属性及其值
        // wx.uploadFile({
        //   // url: 'https://ghlp.lidechenyan.club/upload',
        //   url: 'http://192.168.1.101:8080/change/upload',
        //   filePath: tempFilePath,
        //   name: 'file',
        //   success: function (res) {
        //     var data = res.data;
        //     console.log('data:', data)
        //   }
        // })
        
        
        // ({
        //   src: tempFilePath,
        //   success: function (res) {
        //     console.log(res);
        //     console.log(res.path);
        //     console.log(res.height);
        //     console.log(res.width);
        //   }
        // })
        self.wecropper.pushOrign(tempFilePath)
      }
    })
  },
  onLoad (option) {
    const { cropperOpt } = this.data

    new WeCropper(cropperOpt)
      .on('ready', (ctx) => {
        console.log(`wecropper is ready for work!`)
      })
      .on('beforeImageLoad', (ctx) => {
        console.log(`before picture loaded, i can do something`)
        console.log(`current canvas context:`, ctx)
        wx.showToast({
          title: '上传中',
          icon: 'loading',
          duration: 20000
        })
      })
      .on('imageLoad', (ctx) => {
        console.log(`picture loaded`)
        console.log(`current canvas context:`, ctx)
        wx.hideToast()
      })
      .on('beforeDraw', (ctx, instance) => {
        console.log(`before canvas draw,i can do something`)
        console.log(`current canvas context:`, ctx)
      })
      .updateCanvas()
  }
})
