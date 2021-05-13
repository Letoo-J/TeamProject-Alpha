// pages/goodsDetailsPage/goodsDetailsPage.js
import Toast from '../../components/lib/toast/toast';
Page({
  /**
   * 页面的初始数据
   */
  data: {
    swiperList: [],
    ChineseNum: ['','全新','九成新','八成新','八成新以下'],
    collectState: -1,
    category: "",
    condition: "",
    tagsList: [],
    title: "",
    currentPrice: 0,
    originalPrice: 0,
    description:" ",
    pubId: 0,
    id: 0,
  },

  /**
   * 将新旧程度数字转化为中文数字
   */
  changeNum:function(){
    let that = this;
    let condition = that.data.condition;
    condition = Number(condition);
    let ChineseNum = that.data.ChineseNum[condition];
    that.setData({
      condition:ChineseNum
    })
  },

  /**
   * 获取二手详情页信息
   */
  getGoodsDetail:function(){
    let that = this;
    var session_id = wx.getStorageSync('sessionid');
    var header = {'content-type': 'application/json', 'Cookie': session_id };
    wx.request({
      url: 'http://47.106.241.182:8082/goods/getGoodsDetailById/' + that.data.id,
      method: "GET",
      header: header,
      success(res){
        console.log(res.data);
        if(res.data.code===200){
          that.setData({
            swiperList: res.data.data.picturesLink,
            title: res.data.data.title,
            currentPrice: res.data.data.price,
            originalPrice: res.data.data.originalPrice,
            condition: res.data.data.condition,
            tagsList: res.data.data.tagList,
            collectState: res.data.data.collectState,
            description: res.data.data.description,
            category: res.data.data.category,
            pubId: res.data.data.pubId,
            id:res.data.data.id,
          });
          that.changeNum();
        }
      },
      fail(err){
        console.log(err);
      }
    })
  },
  /**
   * 轮播图图片放大预览
   */
  preview(event) {
    var swiperlist = this.data.swiperList;
    var index = event.currentTarget.dataset.index;
    wx.previewImage({
      current: swiperlist[index],
      urls: swiperlist
    })
  },

  /**
   * 点击home图标跳转首页
   */
  onClickHome(event){
    wx.switchTab({
      url: '/pages/index/index',
    })
  },
  /**
   * 点击收藏变色
   */
  onClickStar(event){
    let that = this;
    var session_id = wx.getStorageSync('sessionid');
    var header = {'content-type': 'application/json', 'Cookie': session_id };
    if(that.data.collectState == -1){
      wx.request({
        url: 'http://47.106.241.182:8082/goods/collectGoods/' + that.data.id,
        method: 'GET',
        header: header,
        success(res){
          console.log(res);
          if(res.data.code===200){
            that.setData({
              collectState: 1
            });
            Toast({
              position: 'bottom',
              message: '收藏成功！'
            });
          } 
        },
        fail(err){
          console.log(err);
        }
      })
    } else if(that.data.collectState == 1) {
      wx.request({
        url: 'http://47.106.241.182:8082/goods/cancelCollectGoods/' + that.data.id,
        method: 'GET',
        header: header,
        success(res){
          console.log(res);
          if(res.data.code===200){
            that.setData({
              collectState: -1
            });
            Toast({
              position: 'bottom',
              message: '取消收藏成功！'
            });
          }
        },
        fail(err){
          console.log(err);
        }
      })
    }
  },
  /**
   * 点击联系卖家跳转
   */
  contact: function() {
    let that = this;
    wx.navigateTo({
      url: '/pages/contact/contact?pubId=' + that.data.pubId,
    })
  },
 /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    wx.setNavigationBarTitle({ title: '二手物品详情页' });
    let goodsID = options.id;
    this.setData({
      id: goodsID,
    })
    this.getGoodsDetail();
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