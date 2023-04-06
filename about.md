# About

## What is it 
Today I Learned XYZ is a website where users can document the interesting
things they learn, either from hobbies or jobs. Users can also read and vote on
the submissions of other users, based on how interesting and useful the
submission is. To stay updated on the findings of the most interesting users,
RSS feeds are available.

## Why I built this 
I built Today I Learned XYZ to learn more about full stack web development, as
well as infrastructure and software engineering in general. However, I wanted
whatever software I made to be useful, with a more specific purpose than just a
generic blogging platform. Before starting the project, I saw this post on
Hacker News: [What To Blog
About](https://simonwillison.net/2022/Nov/6/what-to-blog-about/). I found
particularly inteersting the section about Today I Learned style blog posts, in
which the author writes about new findings that interest them. The TIL format
is mostly for the benefit of the author, as the subject can be something that
has already been covered in other blogs. All that matters was that the author
learned something new and wants to share about it, for the benefit of others. I
thought this would be a refreshing change of pace from social networking sites
where users are constantly trying to make the most popular content.

## How I built this 
I wrote this app in Java, using the Spring framework. I am very impressed by
how mature and usable the ecosystem is, and it has made developing new features
a breeze. Since the user interface mostly a bunch of forms, I chose to write
the front end using the Thymeleaf templating engine. I am a fan of Hacker News,
and I think it's obvious I took inspiration from its clean, minimal UI. The
database side of things was relatively painless with Spring Data JPA, which
implements the most common CRUD methods automatically. I knew the sort of users
I wanted to attract would appreciate RSS feeds, so I used the Rome library to
implement those. To rank posts on the front page, I used a Hacker News-esque
algorithm, as it seems to work pretty well. I also wanted users to have the
option of writing their posts in HTML or Markdown, which the CommonMark library
made effortless. Early on in the project, it became clear that clicking through
the UI to verify each feature would get tedious. Since this was going to be a
large project, I committed to maintaining a suite of unit tests, which really
sped up the development process, even though they aren't the most fun to write.
I knew users would need a way to search through older posts for a particular
topic, so a search engine needed to be added. This feature was more of a
necessity than something I wanted to spend a lot of time implementing, so I
went with Algolia. While I always advocate for free software, this was a
tradeoff I had to make, since I didn't want to spend a lot of time on a feature
that isn't central to the app's purpose. In addition to learning about web
development, I also wanted to learn more about software engineering from this
project, and in today's cloud-centric world, that sometimes means using
services to ease development costs. Despite all this, Algolia and its Java
library have been very easy to work with. One of the last features to add was
verification emails to prevent spam accounts. This feature was very important
to me, because I did not want to see my program become another blog-spam site.
When implementing this feature, I ran into some cryptic error messages
regarding SMTP, but the real issue was not loading the configuration file
correctly. While Spring is great, its error messages can leave something to be
desired. Before deploying to the internet, I needed to clean up the codebase,
as it was littered with references to localhost and API keys. Luckily, Spring
supports multiple profiles, so I could seperate configurations and build the
project either for development or production.
