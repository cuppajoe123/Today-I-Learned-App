const voteButtons = document.querySelectorAll(".upvote");
voteButtons.forEach((button) => {
  button.addEventListener("click", () => {
    fetch("http://today-i-learned.xyz/" + button.getAttribute("data-id"), {
      method: "post",
      redirect: "follow",
    })
      .then((response) => {
        if (response.redirected) return response.text();
        if (response.ok) button.style.visibility = 'hidden';
        throw new Error("No redirect to login");
      })
      .then((text) => {
        document.querySelector("html").innerHTML = text;
      })
      .catch(() => {return});
  });
});
