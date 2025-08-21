import request, { fetch } from '@/utils/request'

// 问答模块-分页
export function questionsList(params) {
  return request.get('/qa/qaQuestions/page', params)
}

// 问答模块-发起提问
export function launchQuestion(body) {
  //console.log(body);
  return request.post('/qa/qaQuestions/create', body)
}

// 问答模块-修改问题
export function updateQuestion(body) {
  return request.post('/qa/qaQuestions/update', body)
}

// 对回答的内容进行点赞(顶)
export function giveLike(body) {
  return request.post('/qa/qaQuestions/giveLike', body)
}

// 回答问题
export function answer(body) {
  return request.post('/qa/qaQuestions/answer', body)
}

//我的问答
export function getMeAnswer(params){
  return request.get('/qa/qaQuestions/self/answer',params);
} 

//我的提问
export function getMeQuestions(params){
  return request.get('/qa/qaQuestions/self/question',params);
}
//我关注的问题
export function getMeFllow(params){
  return request.get('/qa/qaQuestions/self/attention',params);
}
//我收藏的问题
export function getMeCollection(params){
  return request.get('/qa/qaQuestions/self/collect',params);
}

//我的草稿
export function getMeDraft(params){
  return request.get('/qa/qaQuestions/self/draft',params);
}
//获取详情
export function getQuestionDetail(id){
  return request.get(`/qa/qaQuestions/detail/${id}`);
}

export function getGlobalDestinations(){
  return request.get('/qa/destinations/global');
}

//收藏问题
export function setAnswerCollectPost(body){
  return request.post(`/qa/qaQuestions/collect/${body.destinationId}`,body);
};

//取消收藏问题
export function setAnswerCollectUnCollect(body){  
  return request.post(`/qa/qaQuestions/uncollect/${body.destinationId}`,body);
};

//关注
export function setAnswerAttentionPost(body){
  return request.post(`/qa/qaQuestions/focus/${body.destinationId}`,body);
};

//取消关注
export function setAnswerAttentionUnFocus(body){
  return request.post(`/qa/qaQuestions/unfocus/${body.destinationId}`,body)
}

//获取提问目的地列表
export function getAnswerDestList(params){
  return request.get('/mdd/destinations/list',params);
}

//草稿箱问题回显
export function getDraftDetail(id){
 return request.get(`/qa/qaQuestions/draft/${id}`); 
}


export function likeAnswer(answerId) {
  return request.post(`/qa/answer/like/${answerId}`);
}