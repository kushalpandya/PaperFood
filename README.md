# PaperFood

PaperFood is a single page webapp for online bookstore, with very minimal set of features and great UI. It is developed in Java EE with complete MVC architecture. It is developed for college assignment, and is free under GPLv3.

## Important Details

Database design and sample entries for Books used in the project is included "Documents" directory. In case of using MySQL (Community Server 5.1 or higher) you'll need to create **user** `paperfooduser` with **password** `12345` and database `paperfood` and then create tables within the database in order to run project with its default settings (mentioned in `com.paperfood.Config` file).

## Platforms, Tools and Technologies used

* **IDE** - Eclipse 4.2
* **Java** - JDK 1.7 (to be available for JDK 1.6 too)
* **Client-side** - Twitter Bootstrap, jQuery (and UI), [Handlebars](http://handlebarsjs.com/), [JSONify](https://github.com/kushalpandya/JSONify)
* **Development OS** - Ubuntu 12.04 LTS
* **Java Third Party Libs** - [JSON](http://json.org) (for JSON in Java)

## Packages and Web Directory

* `com.paperfood` - Main package, contains Models for Database interaction, Search system and Configuration for Database Interaction.
* `com.paperfood.controller` - Controller Servlets to perform, Registration, Authentication, Process Orders, Show books catalog and search.
* `com.paperfood.entity` - Top-level Entity classes, representing User, Book and Order.
* `com.droidbridge.security` - Hashing algorithm classes.
* `WebContent > assets` - Images and Icons used in PaperFood.
* `WebContent > css` - Stylesheets (CSS and LESS).
* `WebContent > js` - Scripts.
* `WebContent > WEB-INF > lib` - Java Libraries.

## Known Issues

The project was intended to work with SQLite database, and `PaperFood.sqlitedb` file is included with the project along with its connection settings in `com.paperfood.Config` file. On making an entry in `users` table which has `u_id` field as Primary Key and Autoincremented, `INSERT` query fails in SQLite, while it works fine with MySQL.

## Licensing & Sharing

The project was developed just for college assignment, and anyone is free to fork it or reuse its code in their own projects under terms of [GPLv3](http://www.gnu.org/copyleft/gpl.html).

## Author

[Kushal Pandya](https://github.com/kushalpandya)
