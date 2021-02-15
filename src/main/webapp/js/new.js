$(document).ready(function() {
  slider = new Slider();
});

var Slider = (function() {

  var Slider = function() {
    
    this.initialise();
  }

  Slider.prototype = {

    initialise: function() {
      this.vars();
      this.setup();
      this.slideEvent();
    },

    vars: function() {
      // vars
      _this = this;
      this.container = $('.sliders');
      this.slider = $('.slider');
      this.slide = $('.slide');
      this.controls = $('.control');
      this.value = $('.value');
      this.value_in = $('.value_in');
      this.totalPercentage = 100;
    },

    setup: function() {
      // set equal width depending on how many sliders there are when initalised 
      var counter = 0;
      this.slide.each(function() {
        counter++;
      });

      var initWidth = this.totalPercentage / counter;

     this.slide.width(initWidth + '%');
     this.slide.attr('data-percent', initWidth + '%');
     this.value.text(initWidth + '%');
     this.value_in.val(initWidth);
    },

    getPercentWidth: function(target) {
      // get percentage of current width
      target.attr('data-percent', (100 * parseFloat(target.css('width')) / parseFloat(target.parent().css('width'))));
    },

    slideEvent: function() {
      // listen for mouse down on the controls
      this.controls.on('mousedown', function(event) {
        this.slideDrag(event);
      }.bind(this));
    },

    slideDrag: function(event) {
      event.preventDefault();

      this.target = $(event.target);
      this.prevMousePos = 0;

      this.target.parent().addClass('active');
      // listen mousemove and mouseup events on the document: only if the mousedown happend on one of the controls 
      $(document).on('mousemove', this.slideMove);
      $(document).on('mouseup', this.slideEnd);
    },

    slideMove: function() {
      _this.mousePos = event.pageX; 
      _this.amount = [];
      
      // get info on widths, offsets and positions
      var offset = _this.slider.offset().left;
      var sliderWidth = _this.slider.width();
      var posX = Math.min(Math.max(0, _this.mousePos - offset), sliderWidth);   
   
      // checks direction
      if (_this.mousePos < _this.prevMousePos)  {
        _this.direction = 'left';
      } else {
        _this.direction = 'right';
      }
      
      //console.log(_this.direction);
       
      // update mouse position
      _this.prevMousePos = _this.mousePos;
      
      // set new width of the active slider
      _this.target.parent().width(posX / sliderWidth * 100 + '%');
      _this.calcPercent();
      
    },

    calcPercent: function() {
      var totalWidth = 0;
      var sliderLength = 0;
      var leftoverAmount = 0;
      
      // loop through each slide
      _this.slide.each(function() {
        
        sliderLength++;
        _this.getPercentWidth($(this));
        
        if ($(this).hasClass('active')) {
          // set active percentage
          _this.active = parseFloat($(this).attr('data-percent')).toFixed(0);
          

        } else {
         
          // add non active widths into an array
         
          _this.amount.push(parseFloat($(this).attr('data-percent')).toFixed(0));
        }

       //totalWidth += parseFloat($(this).attr('data-percent'));

      });
  
      // find out the leftover amount
      leftoverAmount = _this.totalPercentage - _this.active;
      _this.nonActiveAmount = 0;
      $.each(_this.amount, function() {
          
       _this.nonActiveAmount += parseFloat(this) ;
      });
      
      var x = leftoverAmount / 100;
      var y = _this.nonActiveAmount / 100;
      var z = _this.active;

     
      _this.slide.each(function() {
 
        if (!$(this).hasClass('active') || !$(this).hasClass('locked')) {
           console.log($(this));
         
          var v = x * (parseFloat($(this).attr('data-percent')) / y);
          $(this).width(v + '%');
          $(this).find('.value').text(Math.round(v) + '%');
          $(this).find('.value_in').val(Math.round(v));
             
        }
      });

    },

    slideEnd: function() {
      // kill the events on mouse up.
      _this.target.parent().removeClass('active');
      $(this).off('mousemove', slider.slideMove);
      $(this).off('mouseup', slider.slideEnd);
    },
  }

  return Slider;

}());