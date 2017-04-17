# Project - Fomono

Fomono is an android app that helps users to never miss the things they love. It pulls data from several API's including Eventbrite, Yelp, Fandango, and possibly more to aggregate event data and send user notifications based on new events and/or events happening nearby. Users can choose categories of events/movies and subcategories to cater to their interests. Users can also manually search for events for any location and see what's going down!

Time spent: 60 hours spent in total

## User Stories

The following **required** functionality is completed:
* [X] User can see list of events, restaurants and movies. The list will show title, desc, media Image, etc
* [X] User can scroll through events, restaurants, and movies using view group fragment implementation on home page
* [X] User can make any item as favorite and it will show up under the favorite tab on the main page. The favorites are saved and loaded everytime user comes back to the app
* User can click on the Fomono icon and get directed to
    * [X]	- eventbrite website/app for events
    * [X]	- yelp website/app for restaurants, cafes, etc
    * [X]	- the movie trailer for movies
* [X] User can click anywhere on the list item and see more information in the detail page 
* User can get the results sorted by 
    * [X]	-  best_match, date, distance for events
    * [X]	-  best_match, distance, rating_count for restaurants
* [X] User can get the movie lists which are "Playing now", "Popular" or "Top Rated"
* [X] User can see details of each event in a fragment. Details include title, pic, data/time, price, location, description
* [X] User can configure filter criteria on settings page. filters will be broad category (tech, music, sports, etc)
* [X] User should be able to set location
* [ ] User can click a notification to go to detailed event/restaurant/movie
* [ ] User should be able to turn on/off notifications
* [X] User can scroll endlessly on home page for more events

The following **optional** features are implemented:

* [ ] User will have a profile page to save name, username, exact address(optional), zipcode, status, categories filters
* [ ] User can select a profile image or take picture on profile page
* [ ] User can sign up using Facebook/Google/email
* [ ] User can log in using the above
* [X] User can search for specific events by query string, and filter them on home page
* [X] On event details page, user can add events to calendar
* [X] On event details page, user can see a map of event location
* [ ] Clicking on map could bring up directions to location on google maps
* [ ] On event details page, user can click on a link to the original event page (on eventbrite, or yelp)
* [ ] On movie details page, user can click to see trailer video
* [ ] On event details page, user can share event to Twitter/email/text
* [ ] On home page, user can save events to favorites list
* [ ] On home page, user can choose to see only free or only paid events
* [ ] On home page, user can see the list of restaurants which are "open_now"
* [ ] On home page, user should be able to specify a date range for search events

The following **bonus** features are implemented:

* [ ] User can see past event images/videos on the event details page from Facebook/Twitter/Instagram

The following **additional** features are implemented:

* [ ] Anything else...?

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/vklmmJQ.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

<img src='http://i.imgur.com/qQpgA6L.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

<img src='http://i.imgur.com/gTk5kaL.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes


## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android

## License

    Copyright 2017 Fomono group: Jaspreet Saluja, Malleswari Saranu, David Zhuohao Shao.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
