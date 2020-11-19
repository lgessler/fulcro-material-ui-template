# Introduction

This is a full-stack starter project based on [dvingo](https://github.com/dvingo/dv.fulcro-template)'s excellent 
template which uses [Fulcro](https://fulcro.fulcrologic.com/), 
[Pathom](https://blog.wsscode.com/pathom/v2/pathom/2.2.0/introduction.html),
[Material-UI](https://material-ui.com/), and [Crux](https://opencrux.com/main/index.html) and implements some basic
functionality such as session-based authentication and client-side routing. All code included here is code you'll
probably want to include in your own project, *except* for code that pertains to the `project` entity, which is 
provided as an example.

## Caveats

- **You should not trust this app** when it comes to **security and correctness**. There may be serious security issues
with the code provided. 
- I can't claim that any of this is written in best style--route handling in particular is a little messy.
- Importing Material-UI the way I've done it here will bring _all_ of it into your bundle, which adds a little over
3MB to a production build of the CLJS files here. If you need to bring bundle size down, you should rework the 
Material-UI integration and import only what you need.

# Usage

Note: most common tasks are covered in [the Makefile](./Makefile) -- common cases will be covered below.

## Setup 

1. Clone the repo:

```bash
$ git clone git@github.com:lgessler/fulcro-material-ui-template.git myproject
$ cd myproject
$ rm -rf .git
```

2. Install NPM deps:

```bash
$ yarn install
```

## Development

1. Start your ClojureScript compiler, [`shadow-cljs`](https://github.com/thheller/shadow-cljs), by running `make fe`. 
(`shadow-cljs` will automatically pick up code changes you make to `.cljs` files and the code in this project will hot 
reload it.) You can monitor compilation status at [http://localhost:9630]().

```bash
$ make fe
```

2. In a separate shell session, start the backend server and enter `(start)` in the REPL. You will now be able to go to
[http://localhost:8085]() and have your app served.

```bash
$ make start-dev-server
clojure -A:dev
Clojure 1.10.1
user=> (start)
# navigate to localhost:8085
```

3. If you want to change the project's name from `fmuit` to something else, you'll have to do it manually: search all 
source files for the string `fmuit` and replace it with your desired name. (Be careful as always to use underscores
and hyphens in the proper places. If anyone wants to submit a PR to make this a true template using e.g. 
[clj-new](https://github.com/seancorfield/clj-new) that'd be awesome.)

### Tips

1. See [`src/dev/user.clj`](./src/dev/user.clj) for development REPL setup including a function for database fixtures and 
vars for conveniently accessing those fixtures.

## Deployment

1. Check your configuration options in [src/main/config/prod.edn](./src/main/config/prod.edn).

2. Compile an uberjar:

```bash
make prod-build
```

3. Run your uberjar to make sure it works:

```bash
java -cp deploy/server.jar clojure.main -m fmuit.server.server-entry
```

4. Deploy your JAR.

# Issues

If anything's unclear or you need help, please open an issue, or send me a message on the 
[Clojurians Slack](https://clojurians.slack.com/) (I'm `lgessler`).
