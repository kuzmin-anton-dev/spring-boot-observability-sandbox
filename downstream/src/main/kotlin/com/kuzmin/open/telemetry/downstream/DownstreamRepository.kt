package com.kuzmin.open.telemetry.downstream

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DownstreamRepository : CrudRepository<DownstreamEntity, Long> {
}