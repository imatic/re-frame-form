[![Clojars Project](https://img.shields.io/clojars/v/imatic/re-frame-form.svg)](https://clojars.org/imatic/re-frame-form)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

# re-frame-form

Library to privide some common functionality for forms in [re-frame](https://github.com/Day8/re-frame).

## Installation notes

As this library is using [re-frame-debux](https://github.com/Day8/re-frame-debux), you need to include appropriate library for your env. See [here](https://github.com/Day8/re-frame-debux#two-libraries) for details.

## Api

This section assumes that code is written in file with following requires:
``` clj
(:require
 [re-frame.core :refer [dispatch subscribe]]
 [imatic.re-frame.form.events :as ievents]
 [imatic.re-frame.form.subs :as isubs]
 [imatic.re-frame.form.queries/field-values :refer [field-values]])
```

### Event handlers

#### :imatic.re-frame.form.events/init-form

Initializes form with data.

usage:
```clj
(dispatch [::ievents/init-form {:id :form-id, :initial-data {:email ""}])
```

#### :imatic.re-frame.form.events/clear-form

Removes form from `app-db`.

usage:
```clj
(dispatch [::ievents/clear-form :form-id])
```

#### :imatic.re-frame.form.events/clear-field

Removes field from the form.

usage:
```clj
(dispatch [::ievents/clear-field :form-id :email])
```

#### :imatic.re-frame.form.events/update-field

Updates field value.

usage:
```clj
(dispatch [::ievents/update-field :form-id :email "user@example.com"])
```

#### :imatic.re-frame.form.events/update-errors

Sets errors to the form.

usage:
```clj
(dispatch [::ievents/update-errors :form-id {:email "This value is not valid email"}])
```

### Subscribers

#### :imatic.re-frame.form.subs/form

Retrieves all data for given form.

usage:
```clj
(subscribe [::isubs/form :form-id])
```

#### :imatic.re-frame.form.subs/field-error

Retrieves errors for given field.

usage:
```clj
(subscribe [::isubs/field-error :form-id :email])
```

#### :imatic.re-frame.form.subs/field-value

Retrieves value of given field.

usage:
```clj
(subscribe [::isubs/field-value :form-id :email])
```

#### :imatic.re-frame.form.subs/field-values

Retrieves values for all fields.

usage:
```clj
(subscribe [::isubs/field-values :form-id])
```

#### :imatic.re-frame.form.subs/initialized?

Returns `true` if form is initialized, `false` otherwise.

usage:
```clj
(subscribe [::isubs/field-values :form-id])
```

### Queries

#### imatic.re-frame.form.queries/field-values

Retrieves values of the form from `app-db`.

usage:
```clj
(field-values db :form-id)
```
