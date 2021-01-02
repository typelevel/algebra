---
layout: docs
title:  "Type Classes"
section: "typeclasses"
position: 1
---

# Type Classes

Algebra uses type classes to represent algebraic structures. You can use these type classes to represent the abstract capabilities (and requirements) you want generic parameters to possess.

## Index

{% for x in site.pages %}
{% if x.section == 'typeclasses' %}
- [{{x.title}}]({{site.baseurl}}{{x.url}})
{% endif %}
{% endfor %}

### Documentation Help

We'd love your help with this documentation! You can edit this page in your browser by clicking [this link](https://github.com/typelevel/algebra/edit/master/docs/src/main/tut/typeclasses.md).
