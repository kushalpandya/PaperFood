(function($) {
$.fn.jsonify = function(options) {
	var settings = $.extend({
		stringify : false
	}, options);
	var json = {};
    $.each(this.serializeArray(), function() {
        if (json[this.name]) {
            if (!json[this.name].push)
            	json[this.name] = [json[this.name]];
            json[this.name].push(this.value || '');
        } else
       	 	json[this.name] = this.value || '';
    });
    if(settings.stringify == true)
    	return JSON.stringify(json);
    else
    	return json;
};
})(jQuery);