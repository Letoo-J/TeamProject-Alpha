// pages/comments/comments.js
let app=getApp();
Page({
  onClick(e) {
    if(app.globalData.click==1)
    {
      this.selectComponent('#tabs').resize();
      app.globalData.click=0;
    }
  },

  /**
   * 页面的初始数据
   */
  data: {
    good_secondhand_cmt:[],
    bad_secondhand_cmt:[],
    good_mission_cmt:[],
    bad_mission_cmt:[],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    wx.setNavigationBarTitle({ title: '评论列表' });
    var that=this;
    wx.request({
      //请求获得该用户的二手好评信息
      url: 'http://120.77.210.142:8080/personalCard/getGoodsEvaluationByLike',
      data: {
        id: 1,
        isLike: 1,
      },
      method: 'POST',
      header: {
        "Content-Type": "application/x-www-form-urlencoded",
        'Cookie': wx.getStorageSync('sessionid')
      },
      success: function(res){
        console.log(res);
        that.setData({
          good_secondhand_cmt:res.data.data
        })
      },
      fail: function(res){
        console.log("获取二手好评失败")
      }
    })

    wx.request({
      //请求获得该用户的二手差评信息
      url: 'http://120.77.210.142:8080/personalCard/getGoodsEvaluationByLike',
      data: {
        id: 1,
        isLike: -1,
      },
      method: 'POST',
      header: {
        "Content-Type": "application/x-www-form-urlencoded",
        'Cookie': wx.getStorageSync('sessionid')
      },
      success: function(res){
        console.log(res);
        that.setData({
          bad_secondhand_cmt:res.data.data
        })
      },
      fail: function(res){
        console.log("获取二手差评失败")
      }
    })

    wx.request({
      //请求获得该用户的任务好评信息
      url: 'http://120.77.210.142:8080/personalCard/getTaskEvaluationByLike',
      data: {
        id: 1,
        isLike: 1,
      },
      method: 'POST',
      header: {
        "Content-Type": "application/x-www-form-urlencoded",
        'Cookie': wx.getStorageSync('sessionid')
      },
      success: function(res){
        console.log(res);
        that.setData({
          good_mission_cmt:res.data.data
        })
      },
      fail: function(res){
        console.log("获取任务好评失败")
      }
    })

    wx.request({
      //请求获得该用户的二手差评信息
      url: 'http://120.77.210.142:8080/personalCard/getTaskEvaluationByLike',
      data: {
        id: 1,
        isLike: -1,
      },
      method: 'POST',
      header: {
        "Content-Type": "application/x-www-form-urlencoded",
        'Cookie': wx.getStorageSync('sessionid')
      },
      success: function(res){
        console.log(res);
        that.setData({
          bad_mission_cmt:res.data.data
        })
      },
      fail: function(res){
        console.log("获取任务差评失败")
      }
    })
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