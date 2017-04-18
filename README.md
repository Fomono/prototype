# Project - Fomono

Fomono is an android app that helps users to never miss the things they love. It pulls data from several API's including Eventbrite, Yelp, Fandango, and possibly more to aggregate event data and send user notifications based on new events and/or events happening nearby. Users can choose categories of events/movies and subcategories to cater to their interests. Users can also manually search for events for any location and see what's going down

API (4/17/2017): 
1. Events(EventBrite)
2. Eats(Yelp)
3. Movies(MovieDB)

Time spent: 120 hours spent in total

## User Stories

The following **required** functionality is completed:

Home Page:
* [X] User can see list of events, restaurants and movies. The list will show title, desc, media Image, etc
* [X] User can scroll through events, restaurants, and movies using view group fragment implementation on home page
* [X] User can make any item as favorite and it will show up under the favorite tab on the main page. The favorites are saved and loaded every time user comes back to the app
* [X] User can click on the Fomono icon and get directed to
    * [X] eventbrite website/app for events
    * [X] yelp website/app for restaurants, cafes, etc
    * [X] the movie trailer for movies
* [X] User can click anywhere on the list item and see more information in the detail page 
* [X] User can get the results sorted by 
    * [X] best_match, date, distance for events
    * [X] best_match, distance, rating_count for restaurants
* [X] User can get the movie lists which are "Playing now", "Popular" or "Top Rated"
* [X] User can see details of each event in a fragment. Details include title, pic, data/time, price, location, description
* [X] User can scroll endlessly on home page for more events

Event Detail:
* [X] When a particular view(Event,Eat,movie) is selected from the home page, user is taken to the detail page.
* [X] The detail page's data fields vary based on the type of the view selected.
    * [X] For Events:  Event description, Event Date, Event Location etc
    * [X] For Eats: Restaurant Rating, Location, Hours, Phone Number etc.
    * [X] For Movies: Movie synopsis, Date in theatres, Genre etc.
* [X] User can add an event to their calendar.

Filters:
* [X] User can configure filter criteria on settings page. filters will be broad category (tech, music, sports, etc)
* [X] User should be able to set location

Notifications:
* [X] User can see push notifications sent to their device
* [X] User can click a notification to go to detailed event/restaurant/movie


The following **optional** features are implemented:

Login Page:
* [X] User can login using Facebook
* [X] User is asked permission to access his/her Facebook account to login.
* [X] User can sign-up by registering to an account and setting up his/her own credentials on the application.
    * [X] Name and Email are required fields.
* [X] User can login by using the credentials setup.
* [X] User has an option to skip the sign-in and login as a guest
    * [X] This user's preferences are saved as anonymous and subsequent access from the same device would pull that info.
* [X] User's session is preserved in the subsequent access to the application if they did not sign-out.

User Profile Page:
* [X] When a user is logged in through Facebook,  the user information like Name, email and Profile image and captured and saved into the application.
* [X] User can eventually add/Update that information from the profile page
* [X] User can save/cancel the request to  update the profile information
* [X] User can upload a profile picture  by selecting one from the Photo Gallery or Google Photos.  
    * [ ] User is asked permission to access the gallery.
* [X] User can access the device's camera from the profile page, click a picture and upload it.
    * [X] User is asked permission to access the camera.
    * [ ] If a permission is granted the first time, subsequent requests should directly pull the camera

Events Detail Page:
* [X] For Events/Eats & Movies, User can share the events using Twitter, Mail & Messaging.
* [X] For Eats/Events, User can view the location on a Map view.
    * [X] For the first time, user is asked permission to access their location information. If they deny, subsequent views will no longer prompt the request for permission.
* [X] Clicking on map pin would show details of the event. Show directions when the the link on the map is clicked on. 
* [X] User can mark an event as Favorite which will shows up in the Favorites View Pager tab.
* [X] For Eats, user can view additional pictures in a horizontal scrollable view. 
* [X] For Events/Movies, if the description/synopsis is more than 200 characters, the view is collapsed and can expand touch/click. It can be re-collapsed with a subsequent touch.
* [X] On event details page, user can click on a link to the original event page (on eventbrite, or yelp)
* [ ] On movie details page, user can click to see trailer video

Filters Page:
* [ ] Filters are sorted alphabetically
* [X] Filters are saved automatically everytime a change is made
* [X] Filters are applied automatically when user returns home

Home Page:
* [X] User can search for specific events by query string
* [X] On home page, user can save events to favorites list
* [ ] On home page, user can choose to see only free or only paid events
* [ ] On home page, user can see the list of restaurants which are "open_now"
* [ ] On home page, user should be able to specify a date range for search events

Notifications:
* [ ] User should be able to turn on/off notifications


The following **bonus** features are implemented:

* [ ] User can see past event images/videos on the event details page from Facebook/Twitter/Instagram


## Video Walkthrough

Here's a walkthrough of implemented user stories:

Sprint 2 GIFs:
1. http://i.imgur.com/dcGdgRW.mp4
2. http://i.imgur.com/m9HH960.mp4
3. http://i.imgur.com/Or8Pds6.gif
4. http://i.imgur.com/QFhSQGU.mp4
5. http://i.imgur.com/W2roEKw.mp4

Sprint 1 GIFs:
1. http://i.imgur.com/vklmmJQ.gif
2. http://i.imgur.com/qQpgA6L.gif
3. http://i.imgur.com/gTk5kaL.gif

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
