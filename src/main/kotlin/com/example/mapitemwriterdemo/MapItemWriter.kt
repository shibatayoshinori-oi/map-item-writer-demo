package com.example.mapitemwriterdemo

import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.support.ListItemWriter
import java.util.concurrent.ConcurrentHashMap

/**
 * [Map]に書き込む[ItemWriter]
 * 本ItemWriterを[JobScope]としてBean定義することで、書き込まれた[Map]を[writtenItems]で取得して後続Stepで利用できる。
 *
 * @param K [Map]のキーの型
 * @param V [Map]の値の型
 *
 * @see ListItemWriter
 */
open class MapItemWriter<K, V>(
    /** [Map]のキーを取得する関数 */
    private val keySelector: (V) -> K
) : ItemWriter<V> {

    private val internalWrittenItems = ConcurrentHashMap<K, V>()

    /** 書き込まれた[Map] */
    val writtenItems: Map<K, V> get() = internalWrittenItems.toMap()

    override fun write(items: List<V>) {
        items.forEach { internalWrittenItems[keySelector(it)] = it }
    }
}
