function loadMoreMessages() {
    let currentPage = parseInt(document.getElementById('currentPage').value);
    let nextPage = currentPage + 1;
    let pageSize = 10;
    let url = '/messages/more?page=' + nextPage + '&size=' + pageSize;

    $.get(url, function(data) {
        if (data.length > 0) {
            data.forEach(function(message) {
                let row = '<tr>' +
                    '<td>' + message.title + '</td>' +
                    '<td>' + message.content + '</td>' +
                    '<td>' + message.user.username + '</td>' +
                    '<td>' + message.date + '</td>' +
                    '</tr>';
                $('#messageTableBody').append(row);
            });
            document.getElementById('currentPage').value = nextPage;
        } else {
            alert('No more messages to load.');
        }
    });
}

$(window).scroll(function() {
    if ($(window).scrollTop() === $(document).height() - $(window).height()) {
        loadMoreMessages();
    }
});
