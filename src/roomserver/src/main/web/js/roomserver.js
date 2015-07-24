;(function($) {

    var app = $.sammy(function() {
        this.use(Sammy.EJS);

        function setActive(name) {
            $('#topBar li').removeClass('active');
            $('#topBar li.' + name).addClass('active');
        }

        // Paths
        this.get('#/', function() {
            this.render('templates/index.ejs', function(html) {
                $('#mainContent').html(html);
                setActive('home');
            });
        });
    });
    $(function () {
        app.run('#/');
    });

})(jQuery);