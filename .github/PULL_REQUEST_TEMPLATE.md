<script>
window.addEventListener('DOMContentLoaded', (event) => {
    // 브랜치 이름에서 이슈 번호 추출
    const branchName = document.querySelector('.commit-ref').textContent;
    const match = branchName.match(/GROU-(\d+)/);
    
    if (match) {
        const issueNumber = match[1];
        const titleInput = document.getElementById('pull_request_title');
        if (titleInput && !titleInput.value.includes(`[GROU-${issueNumber}]`)) {
            titleInput.value = `[GROU-${issueNumber}] ${titleInput.value}`;
        }
    }
});
</script>

## 📌 변경사항
<!-- 변경사항에 대한 요약을 작성해주세요 -->
- 

## ✅ 체크리스트
- [ ] 모든 테스트가 통과했나요?
- [ ] 관련 문서를 업데이트했나요? (필요한 경우)

## 📝 테스트
<!-- 어떤 테스트를 진행했는지 설명해주세요 -->
- 

## 💡 추가 정보
<!-- 리뷰어가 알아야 할 추가 정보가 있다면 작성해주세요 -->