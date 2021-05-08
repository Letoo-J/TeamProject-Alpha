// pages/changeInf/changeInf.js
let app=getApp();
Page({
  /**
   * 页面的初始数据
   */
  data: {
      imgsrc:app.globalData.imgsrc,
      username:app.globalData.username,
      phonenum:app.globalData.phonenum,
      qqnum:app.globalData.qqnum,
  },

  change: function() {
    wx.navigateTo({
      url: '/pages/change/change',
    })
  },

  logout: function() {
    wx.showModal({
      content: '确认退出当前账号吗？',
      success: function (res) {
        if (res.confirm) {
          wx.navigateTo({
            url: '/pages/login/login',
          })
          console.log('用户点击确定');
        } else if (res.cancel) {
          console.log('用户点击取消');
        }
      }
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    wx.setNavigationBarTitle({ title: '个人信息' })  

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.setData({
      imgsrc:app.globalData.imgsrc,
      username:app.globalData.username,
      phonenum:app.globalData.phonenum,
      qqnum:app.globalData.qqnum,
    })
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    wx.reLaunch({
      url: '/pages/myInf/myInf'
    })
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})