<template>
  <div class="main-container">
    <div class="wrapper wrapper-new clearfix">
      <div class="col-main">
        <div class="newcate-wrap _j_qa_list">
          <div class="hd">
            <ul class="cate-tab">
              <li class="_j_change_type" :class="{ on: currentTab === '0' }" @click="changeTab('0')">
                <a> 热门问题 </a>
              </li>
              <li class="_j_change_type" :class="{ on: currentTab === '1' }" @click="changeTab('1')">
                <a> 最新问题 </a>
              </li>
              <li class="_j_change_type" :class="{ on: currentTab === '2' }" @click="changeTab('2')">
                <a> 待回答问题 </a>
              </li>
            </ul>
          </div>
          <div class="bd newcate-bd">
            <ul class="_j_pager_box">
              <li v-for="item in questionsData" :key="item.id" class="item clearfix">
                <div class="title">
                  <a :href="`/questions/details/${item.id}`" target="_blank">{{ item.title }}</a>
                </div>
                <div class="container">
                  <div class="avatar">
                    <a :href="`/questions/details/${item.id}`" target="_blank" class="_j_filter_click">
                      <img class="_j_filter_click" :src="item.user?.headImgUrl || '/default.jpg'"/>
                    </a>
                  </div>
                  <div class="user-info">
                    <a class="name _j_filter_click" href="javascript:;">{{ item.user?.nickname || '匿名用户' }}</a>
                    <a class="level _j_filter_click" href="javascript:;" rel="nofollow">LV.{{
                        item.user?.level ?? 1
                      }}</a>
                  </div>
                  <div class="identity">
                    <a class="i-guide _j_filter_click" href="" target="_blank">指路人</a>
                  </div>
                  <div class="desc clearfix">
                    <a :href="`/questions/details/${item.id}`" target="_blank" style="color: #666">
                      <img :src="item.coverUrl" width="150" height="100" v-if="item.coverUrl"/>
                      <p>{{ item.detailDoubt }}</p>
                    </a>
                  </div>

                  <div class="tags">
                    <a v-for="item2 in item.allLabelList" :key="item2" class="a-tag _j_filter_click" href="">{{
                        item2
                      }}</a>
                  </div>
                  <div class="operate">
                    <div class="zan"><i></i>0</div>
                    <div class="mdd">
                      <a href="" class="_j_filter_click" target="_blank"><i
                          class="_j_filter_click"></i>{{ item.destinationName }}</a>
                    </div>
                    <div class="cate-share" @click.stop>
                      <a class="share-btn _js_showShare _j_filter_click" href="javascript:void(0)" @click.prevent="toggleShare(item.id)">
                        分享
                      </a>
                      <div
                          class="share-pop clearfix"
                          v-show="shareVisibleId === item.id"
                          @click.stop
                      >
                        <a title="分享到新浪微博" class="sina" @click.prevent="shareTo('weibo', item)"></a>
                        <a title="分享到QQ空间" class="zone" @click.prevent="shareTo('qzone', item)"></a>
                        <a title="分享到微信" class="weixin" @click.prevent="shareTo('wechat', item)"></a>
                      </div>
                    </div>

                    <a :href="`/questions/details/${item.id}#comment`" target="_blank" style="text-decoration: none">
                      <span class="reply">评论 ({{ item.commentCount }})</span>
                    </a>
                    <span class="date"><i></i>发布于{{ formatCreateTime(item.createTime) }}</span>
                  </div>
                </div>
              </li>
            </ul>
          </div>
          <div class="_j_pager">
            <div class="ft ft_load_btn">
              <div v-if="questionsData.length < questionsDataTotal && !loading" class="answer-more _j_add_more_button"
                   @click="loadMore">
                <a>加载更多<i></i></a>
              </div>
            </div>
            <div class="ft ft_loading _j_add_loading_button hide">
              <div class="answer-loading">正在加载<i></i></div>
            </div>
          </div>
        </div>
      </div>
      <div class="col-side">
        <div class="i-ask">
          <NuxtLink to="/questions/publish" class="btn-ask btn-ask2"><i></i>我要提问</NuxtLink>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import {questionsList} from '@/composables/api/questions'

import { ref, onMounted, nextTick } from 'vue'

const shareVisibleId = ref(null) // 记录哪个问题的分享弹窗打开了，null 表示全部关闭

function toggleShare(id) {
  if (shareVisibleId.value === id) {
    shareVisibleId.value = null
  } else {
    shareVisibleId.value = id
  }
}

function closeShare() {
  shareVisibleId.value = null
}

// 点击页面其他地方关闭弹窗，监听全局点击事件
onMounted(() => {
  document.addEventListener('click', (e) => {
    // 如果点击的是分享按钮或弹窗本身，什么也不做
    const target = e.target as HTMLElement
    if (
        target.closest('.cate-share') ||
        target.classList.contains('share-btn') ||
        target.closest('.share-pop')
    ) {
      return
    }
    // 否则关闭弹窗
    closeShare()
  })
})
const currentTab = ref('0')
const questionsParams = reactive({
  currentPage: 1,
  pagesize: 10,
  tab: '0'      // 新增，和currentTab初始值对应
})
const questionsDataTotal = ref(0)
const questionsData = ref([])


const formatCreateTime = (timeStr: string): string => {
  if (!timeStr) return ''

  const now = new Date()
  const createTime = new Date(timeStr)
  const diffSeconds = Math.floor((now.getTime() - createTime.getTime()) / 1000)

  if (diffSeconds < 60) return '刚刚'
  if (diffSeconds < 3600) return `${Math.floor(diffSeconds / 60)}分钟前`
  if (diffSeconds < 86400) return `${Math.floor(diffSeconds / 3600)}小时前`
  return `${Math.floor(diffSeconds / 86400)}天前`
}

onMounted(() => {
  nextTick(() => {
    getQuestionsList()
  })
})


function shareTo(platform: string, item) {
  const url = encodeURIComponent(`https://yourdomain.com/questions/details/${item.id}`)
  const title = encodeURIComponent(item.title)
  const summary = encodeURIComponent(item.detailDoubt || '')
  const imageUrl = encodeURIComponent(item.coverUrl || '')

  if (platform === 'qzone') {
    window.open(
        `https://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url=${url}&title=${title}&summary=${summary}&pics=${imageUrl}`,
        '_blank'
    )
  } else if (platform === 'weibo') {
    window.open(
        `https://service.weibo.com/share/share.php?url=${url}&title=${title}&pic=${imageUrl}`,
        '_blank'
    )
  } else if (platform === 'wechat') {
    alert('请使用浏览器微信扫码功能分享，或者用微信扫一扫打开链接：\n' + decodeURIComponent(url))
  }
}

function changeTab(tab) {
  if (currentTab.value === tab) return

  currentTab.value = tab
  questionsParams.currentPage = 1
  questionsParams.tab = tab

  // 不要立刻清空数据，先显示 loading 再请求数据
  getQuestionsList(true) // 标记为切换 tab 请求
}


const loading = ref(false)

function getQuestionsList(isTabChange = false) {
  if (loading.value) return
  loading.value = true

  questionsList(questionsParams)
      .then(res => {
        if (questionsParams.currentPage === 1) {
          // 如果是切换 tab，再替换数据（此时请求已返回）
          questionsData.value = res.data.records
        } else {
          // 加载更多场景
          questionsData.value = questionsData.value.concat(res.data.records)
        }
        questionsDataTotal.value = res.data.total
      })
      .catch(err => {
        console.error(err)
      })
      .finally(() => {
        loading.value = false
      })
}


function loadMore() {
  if (loading.value) return    // 请求中不允许继续加载
  if (questionsData.value.length >= questionsDataTotal.value) return  // 已加载全部，禁止再加载

  questionsParams.currentPage++
  getQuestionsList()
}

</script>
<style lang="scss" scoped>
.main-container {
  position: relative;
  padding-top: 30px;

  .wrapper {
    width: 980px;
    margin: 0 auto;

    .col-main {
      float: left;
      width: 685px;

      .newcate-wrap {
        font-size: 14px;
        margin-bottom: 100px;

        .hd {
          margin-bottom: 10px;
          height: 32px;
          line-height: 32px;

          ul {
            float: left;

            li {
              float: left;
              display: inline;

              &.on {
                a {
                  background-color: #192885;
                  color: #fff;
                }
              }

              a {
                display: block;
                padding: 0 5px;
                font-size: 16px;
                color: #666;
                padding: 0 20px;
                border-radius: 3px;
              }
            }
          }
        }

        .newcate-bd {
          clear: both;
          margin-bottom: 20px;

          li {
            padding: 15px 0;
            border-bottom: 1px solid #e5e5e5;
            cursor: pointer;

            .title {
              font-size: 18px;
              line-height: 30px;
              color: #333;
              margin-bottom: 12px;

              a {
                color: #333;

                &:hover {
                  color: #192885;
                }
              }
            }

            .container {
              padding-left: 60px;
              position: relative;

              .avatar {
                width: 48px;
                height: 48px;
                border-radius: 50%;
                border: 1px solid #e5e5e5;
                overflow: hidden;
                position: absolute;
                left: 0;
                top: 0;

                img {
                  width: 100%;
                  height: 100%;
                  border-radius: 50%;
                }
              }

              .user-info {
                width: 100%;
                height: 18px;
                line-height: 18px;
                margin-bottom: 6px;

                .name {
                  margin-right: 5px;
                }

                .level {
                  color: #d20000 !important;
                  font-size: 10px;
                  font-family: Verdana;
                  font-weight: bold;
                }
              }

              .identity {
                width: 100%;
                height: 20px;
                margin-top: 9px;
                margin-bottom: 12px;

                a.i-guide {
                  display: inline-block;
                  height: 18px;
                  margin-right: 4px;
                  padding: 0 4px 0 20px;
                  border-width: 1px;
                  border-style: solid;
                  border-radius: 10px;
                  font-size: 12px;
                  line-height: 18px;
                  color: #fff;
                  position: relative;
                  background: url(../../assets/images//mfwask-2016sprite-2x_v6.png) no-repeat;
                  background-size: 50px;
                  border-color: #4fb3a8;
                  background-color: #70c2b9;
                  background-position: 4px -517px;
                  padding-left: 26px;
                }
              }

              .desc {
                line-height: 24px;
                color: #666;

                img {
                  width: 150px;
                  height: 100px;
                  float: left;
                  margin-right: 16px;
                }

                p {
                  padding-top: 2px;
                  overflow: hidden;
                }
              }

              .tags {
                height: 22px;
                margin-top: 14px;

                a {
                  display: block;
                  float: left;
                  margin: 3px 5px 0 0;
                  padding: 0 15px;
                  background-color: #f6f6f6;
                  border: 1px solid #e5e5e5;
                  border-radius: 10px;
                  font-size: 12px;
                  line-height: 20px;
                  color: #666;
                  white-space: nowrap;

                  &:hover {
                    background-color: #192885;
                    border-color: #192885;
                    color: #fff;
                    text-decoration: none;
                  }
                }
              }

              .operate {
                width: 100%;
                height: 16px;
                margin-top: 16px;
                position: relative;
                font-size: 12px;
                line-height: 16px;
                text-align: right;
                color: #999;

                .zan {
                  float: left;

                  i {
                    float: left;
                    display: inline-block;
                    width: 30px;
                    height: 26px;
                    background: url(../../assets/images/like30x26@2x.png) no-repeat;
                    background-size: 30px auto;
                    overflow: hidden;
                    margin-right: 9px;
                    margin-top: -5px;
                    margin-bottom: -5px;
                  }
                }

                .mdd {
                  display: inline-block;
                  padding-right: 7px;
                  color: #192885;

                  a {
                    color: #192885;

                    i {
                      width: 10px;
                      height: 12px;
                      background: url(../../assets/images/icon-mdd-2x.png) no-repeat;
                      background-size: 100% 100%;
                      display: inline-block;
                      margin-right: 6px;
                      vertical-align: middle;
                      position: relative;
                      top: -1px;
                    }
                  }
                }




                .cate-share {
                  display: inline-block;
                  padding: 0 10px;
                  position: relative;
                  border-left: 1px solid #e5e5e5;
                  cursor: pointer;

                  a.share-btn {
                    color: #999;
                    text-decoration: none;
                    user-select: none;
                  }

                  .share-pop {
                    position: absolute;
                    bottom: 100%;
                    right: 0;
                    margin-bottom: 6px;
                    background-color: #fff;
                    border: 1px solid #e5e5e5;
                    border-radius: 6px;
                    padding: 6px 10px;
                    white-space: nowrap;
                    display: flex;
                    gap: 12px;
                    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
                    z-index: 1000;

                    a {
                      display: inline-block;
                      width: 36px;
                      height: 36px;
                      border-radius: 5px;
                      background-repeat: no-repeat;
                      background-position: center;
                      background-size: 24px 24px;
                      cursor: pointer;
                      transition: transform 0.3s ease;

                      &:hover {
                        transform: scale(1.2);
                      }
                    }
                    a.sina {
                      background-color: #d81e06; /* 微博红色 */
                      background-image: url('https://img.icons8.com/color/48/000000/weibo.png');
                      background-position: center;
                      background-size: 24px 24px;
                    }

                    a.zone {
                      background-color: #0078ff; /* QQ空间蓝 */
                      background-image: url('https://img.icons8.com/color/48/000000/qq.png');
                      background-position: center;
                      background-size: 24px 24px;
                    }

                    a.weixin {
                      background-color: #09bb07; /* 微信绿色 */
                      background-image: url('https://img.icons8.com/color/48/000000/wechat.png'); /* 微信图标 */
                      background-position: center;
                      background-size: 24px 24px;
                    }

                  }
                }









                a {
                  color: #999;
                }

                span {
                  display: inline-block;
                  border-left: 1px solid #e5e5e5;
                  padding: 0 7px 0 10px;
                }

                .date {
                  padding-right: 0;

                  i {
                    width: 12px;
                    height: 16px;
                    background: url(../../assets/images/mfw-ask-sprite20.png) no-repeat -100px -100px;
                    background-size: 182px 550px;
                    display: inline-block;
                    margin-right: 6px;
                    vertical-align: middle;
                    position: relative;
                    top: -1px;
                  }
                }
              }
            }
          }
        }

        ._j_pager {
          .ft {
            margin: 20px 0;

            .answer-more {
              width: 100%;
              height: 40px;
              border-radius: 4px;
              line-height: 40px;
              text-align: center;

              a {
                display: block;
                width: 100%;
                height: 100%;
                background-color: #efefef;
                color: #666;

                &:hover {
                  background-color: #fff5e5;
                  color: #192885;
                  text-decoration: none;

                  i {
                    background-position: 0 -347px;
                  }
                }

                i {
                  display: inline-block;
                  width: 10px;
                  height: 10px;
                  background: url(../../assets/images/mfwask-2016sprite-2x_v3.png) no-repeat;
                  background-position: 0 -329px;
                  background-size: 50px 400px;
                  margin-left: 6px;
                }
              }
            }

            .answer-loading {
              width: 100%;
              height: 40px;
              border-radius: 4px;
              line-height: 40px;
              text-align: center;
              background-color: #fff5e5;
              color: #192885;

              i {
                display: inline-block;
                width: 22px;
                height: 4px;
                background: url(../../assets/images/mfwask-2016loading.gif) no-repeat;
                margin-left: 6px;
              }
            }
          }
        }
      }
    }

    .col-side {
      float: right;
      width: 260px;

      .i-ask {
        margin-bottom: 30px;

        .btn-ask {
          display: block;
          border-radius: 4px;
          margin-top: 73px;
          height: 46px;
          background-color: #f4f4f4;
          line-height: 46px;
          color: #ff8a00;
          font-size: 16px;
          text-align: center;
          overflow: hidden;

          &.btn-ask2 {
            background-color: #192885;
            text-decoration: none;
            color: #fff;

            i {
              background-position: -30px 0;
            }
          }

          i {
            display: inline-block;
            margin: 0 9px 0 0;
            width: 15px;
            height: 15px;
            background: url(../../assets/images/mfw-ask-sprite19.png) no-repeat -30px -15px;
            overflow: hidden;
            vertical-align: -2px;
          }
        }
      }
    }
  }

  .hide {
    display: none;
  }
}
</style>
