package com.mateuszkrawczuk.TvProgramSearcher.common.di

import com.mateuszkrawczuk.tvprogramsearcher.common.repository.createDb

interface IDatabaseDependencyProvider

fun createDbClient(
    dependencyProvider: IDatabaseDependencyProvider = object : IDatabaseDependencyProvider {}
) = createDb(dependencyProvider)