<template>
  <el-container>
    <el-header></el-header>
    <el-row class="goodsTable">
      <el-table :data="activitiesReports" stripe border height="400">
        <el-table-column prop="id" label="序号" width="70"></el-table-column>
        <el-table-column prop="reportedInfoId" label="订单ID" width="70"></el-table-column>
        <el-table-column prop="title" label="标题" width="200"></el-table-column>
        <el-table-column prop="whistleblowerId" label="举报人ID" width="90"></el-table-column>
        <el-table-column prop="content" label="举报理由"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
                size="mini"
                @click="viewDetail(scope.$index, scope.row)">查看</el-button>
            <el-button
                size="mini"
                type="danger"
                @click="ignoreReport(scope.$index, scope.row)">忽略</el-button>
            <el-button
                size="mini"
                type="primary"
                @click="throughReport(scope.$index, scope.row)">通过</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <div class="page">
      <el-pagination
          @current-change="handleCurrentChange"
          @pre-click="handleCurrentChange"
          @next-click="handleCurrentChange"
          :current-page.sync="currentPage"
          :page-size="10"
          layout="total, prev, pager, next"
          :total="totalNum">
      </el-pagination>
    </div>
  </el-container>
</template>

<script>
import axios from "axios";

export default {
  data () {
    return {
      currentPage: 1,
      totalNum: 53,
      activitiesReports: [
        {
          "content": "zzz",
          "gmtInfo": {
            "gmtCreate": 1623340070000,
            "gmtModified": 1623340071000
          },
          "goodsReportMessageDO": {
            "id": 4,
            "reason": "zzz",
            "reportedGoodsId": 2,
            "state": 0,
            "title": "xxx",
            "whistleblowerId": 1
          },
          "id": 4,
          "reportedInfoId": 2,
          "state": 0,
          "title": "xxx",
          "type": 1,
          "whistleblowerId": 1
        }
      ]
    }
  },
  methods: {

    viewDetail(index,row) {
      console.log(row.reportedInfoId);
      this.$router.push({name: "activityDetail", params: {'id': row.reportedInfoId}})
    },//查看详情

    ignoreReport(index,row) {
      console.log(row.id);
      let url = "/api/admin/ignoreActivityReport/" + row.id;
      axios.get(url);
    },//忽略举报

    throughReport(index,row) {
      console.log(row.id);
      let url = "/api/admin/handleActivityReport/" + row.id;
      axios.get(url);
    },//通过举报

    handleCurrentChange(val) {
      this.currentPage = val;
      this.getReports();
    },//改变页面

    getReports() {
      let url = "/api/admin/activitiesReport/page/" + this.currentPage;
      axios.get(url).then(res => {
        this.activitiesReports = res.data.data.activitiesReports;
        this.currentPage = res.data.data.pageInfo.currentPage;
        this.totalNum = res.data.data.pageInfo.totalNum;
      })
    }
  },
  beforeMount:function () {
    this.getReports();
  },

}
</script>

<style>
.goodsTable {
  margin: 20px 10%;
}
.page {
  margin: 0 auto;
}
</style>