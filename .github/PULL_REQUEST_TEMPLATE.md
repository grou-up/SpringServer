# [<JIRA_KEY>] PR Title

## 📝 Description
<!-- PR에 대한 설명을 작성해주세요 -->


## 📌 Changes
<!-- 변경사항들을 리스트로 작성해주세요 -->
- 

## ✅ Check List
- [ ] 테스트 코드가 있다면 추가했나요?
- [ ] 새로운 의존성을 추가했다면 문서화했나요?
- [ ] 변경사항에 대한 테스트를 진행했나요?
- [ ] 코드 컨벤션을 지켰나요?

## 📸 Screenshots
<!-- UI 변경사항이 있다면 스크린샷을 첨부해주세요 -->


<!-- 
아래 내용은 자동으로 채워집니다. 수정하지 마세요.
브랜치명이 'feature/PROJ-123-description' 형식이라면,
PR 생성 시 자동으로 [PROJ-123]이 제목에 추가됩니다.
-->
<script>
window.addEventListener('DOMContentLoaded', (event) => {
    const branchName = document.querySelector('.commit-ref').textContent;
    const jiraKey = branchName.match(/(?:feature|bugfix)\/([A-Z]+-\d+)/)?.[1];
    if (jiraKey) {
        const titleInput = document.getElementById('pull_request_title');
        if (!titleInput.value.includes(jiraKey)) {
            titleInput.value = `[${jiraKey}] ${titleInput.value}`;
        }
    }
});
</script>